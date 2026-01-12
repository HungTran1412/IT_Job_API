package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.configuration.AppProperties;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateSearchRequest;
import backend.main.dto.response.CandidateResponse;
import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.entities.VerificationToken;
import backend.main.enums.Code;
import backend.main.enums.NotificationType;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.JobRepository;
import backend.main.repository.NotificationRepository;
import backend.main.repository.VerificationTokenRepository;
import backend.main.services.CandidateService;
import backend.main.services.NotificationService;
import backend.main.specification.CandidateSpec;
import backend.main.utils.CloudinaryFileUpload;
import backend.main.utils.JwtUtils;
import backend.main.utils.SendEmailHandler;
import backend.main.utils.SseUtils;
import backend.main.utils.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class CandidateServiceImpl implements CandidateService {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    SendEmailHandler sendEmailHandler;
    @Autowired
    CloudinaryFileUpload cloudinaryFileUpload;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    AppProperties appProperties;
    @Autowired
    JobRepository jobRepository;
    
    NotificationRepository notificationRepository;
    
    NotificationService notificationService;
    SseUtils sseUtils;

    private String generateCandidateID() {
        return "USER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public Candidate register(CandidateRegisterRequest candidateRequest) {
        //kiem tra tinh hop le
        ValidationUtils.validateEmail(candidateRequest.getEmail());
        ValidationUtils.validatePassword(candidateRequest.getPassword());

        if(candidateRepository.findByEmail(candidateRequest.getEmail()).isPresent()){
            throw new AppException(Code.EMAIL_EXISTED);
        }
        // Tạo đối tượng Candidate mới
        Candidate candidate = Candidate.builder()
                .candidateId(generateCandidateID())
                .fullname(candidateRequest.getFullName())
                .email(candidateRequest.getEmail())
                .password(passwordEncoder.encode(candidateRequest.getPassword())) // Mã hóa mật khẩu
                .role(Role.ROLE_CANDIDATE)
                .createdAt(LocalDateTime.now())
                .likedJobs(new ArrayList<Job>())
                .applications(new ArrayList<Application>())
                .enabled(false)
                .isPrivate(true)
                .build();

        saveCandidate(candidate);

        //sinh token ngau nhien
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationToken.setCandidate( candidate );

        verificationTokenRepository.save(verificationToken);

        //gan link + token vao email va gui
        String verifyLink = appProperties.getBaseUrl() + appProperties.getVerify().getCandidate() + token;
        sendEmailHandler.sendVerificationEmail(candidate.getEmail(), verifyLink);

        //luu nguoi dung
        return candidate;
    }

    @Override
    @Transactional
    public void resendVerification(String email) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        if (candidate.getEnabled() == true) {
            throw new AppException(Code.ACCOUNT_VERIFIED);
        }

        // Xóa token cũ nếu đã tồn tại
        verificationTokenRepository.findByCandidate(candidate)
                .ifPresent(verificationTokenRepository::delete);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        VerificationToken newToken = new VerificationToken();
        newToken.setToken(token);
        newToken.setCandidate(candidate);
        newToken.setExpirationTime(LocalDateTime.now().plusSeconds(30));

        verificationTokenRepository.save(newToken);

        String verifyLink = appProperties.getBaseUrl() + appProperties.getVerify().getCandidate() + token;
        sendEmailHandler.sendVerificationEmail(candidate.getEmail(), verifyLink);
    }


    @Override
    @Transactional
    public Candidate verifyCandidate(String token){
        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        if(vt.getExpirationTime().isBefore(LocalDateTime.now())){
            Candidate c = vt.getCandidate();
            verificationTokenRepository.delete(vt);
            candidateRepository.delete(c);
            throw new AppException(Code.TOKEN_EXPIRED);
        }

        Candidate cd = vt.getCandidate();

        cd.setUpdateAt(LocalDateTime.now());
        cd.setEnabled(true);
        verificationTokenRepository.delete(vt);
        Candidate savedCandidate = saveCandidate(cd);

        // Gửi email chào mừng sau khi xác thực thành công
        sendEmailHandler.sendWelcomeEmail(savedCandidate.getEmail(), savedCandidate.getFullname());

        // Notify admin
        String adminContent = "Ứng viên mới đã đăng ký: " + savedCandidate.getFullname();
        
        Long id = notificationService.saveNotification(
        		appProperties.getAdmin().getEmail(), 
        		Role.ROLE_ADMIN,
        		NotificationType.SYSTEM,
        		adminContent, 
        		savedCandidate.getEmail());
        
    
        sseUtils.sendToUser(appProperties.getAdmin().getEmail(), adminContent, id);


        return savedCandidate;
    }

    @Override
    @Transactional
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try {
            Candidate c = candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

            if(c.getPassword() == null){
                throw new AppException(Code.PASSWORD_IS_NULL);
            }

            //kiểm tra mật khẩu cũ trước khi đổi
            if(!passwordEncoder.matches(oldPassword,c.getPassword())){
                throw new AppException(Code.OLD_PASSWORD_NOT_MATCH);
            }

            c.setPassword(passwordEncoder.encode(newPassword));
            saveCandidate(c);
            return true;
        } catch (AppException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public String login(LoginRequest request) {
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePassword(request.getPassword());

        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Candidate c = candidateRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(request.getPassword(), c.getPassword())) {
            throw new AppException(Code.WRONG_PASSWORD);
        }

        if(c.getEnabled() == false){
            throw new AppException(Code.ACCOUNT_UNENABLED);
        }
        
        if(c.getIsLocked()){
            throw new AppException(Code.ACCOUNT_LOCKED);
        }

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return jwtUtils.generateToken(c.getCandidateId(),c.getEmail(), c.getRole(), request.isRememberMe());
    }

    @Override
    @Transactional
    public Candidate updateInfo(String id, CandidateRequest request) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        // Kiểm tra nếu người dùng cố tình cập nhật trạng thái khóa
        if (c.getIsLocked() == true) {
            throw new AppException(Code.ACCOUNT_LOCKED);
        }

        c.setFullname(request.getFullname());
        c.setGender(request.getGender());
        c.setPhone(request.getPhone());
        c.setDateOfBirth(request.getDateOfBirth());
        c.setAddress(request.getAddress());
        c.setUpdateAt(LocalDateTime.now());
        c.setSoftSkill(request.getSoftSkill());
        c.setExperience(request.getExperience());
        c.setIsPrivate(request.getIsPrivate());
        c.setDesiredSalary(request.getDesiredSalary());
        c.setTechnologies(request.getTechnologies());


        //Kiem tra xem nguoi dung co cap nhat anh khong
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            System.out.println("Image: " + request.getAvatar().getOriginalFilename());
            String imgUrl = cloudinaryFileUpload.uploadImage(request.getAvatar());
            c.setAvatar(imgUrl);
        }
//        else if(request.getAvatar().toString() == "null"){
//            String imgUrl = c.getAvatar();
//            c.setAvatar(imgUrl);
//            System.out.println("Logo: " + imgUrl);
//        }

//        Kiểm tra người dùng có thêm cv không
        if(request.getCv() != null && !request.getCv().isEmpty()){
            System.out.println("CV: " + request.getCv().getOriginalFilename());
            String url = cloudinaryFileUpload.uploadCv(request.getCv());
            c.setCv(url);
        }else{
            String url = c.getCv();
            c.setCv(url);
            System.out.println("CV: " + url);
        }

        return saveCandidate(c);
    }

    private Candidate saveCandidate(Candidate c) {
        return candidateRepository.save(c);
    }

	@Override
	@Transactional
	public boolean addLikedJob(String jobId, String candicateId) {
	    Job job = jobRepository.findById(jobId)
	            .orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));

	    Candidate candidate = candidateRepository.findById(candicateId)
	            .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

	    // Nếu đã thích rồi thì khỏi thêm
	    if (candidate.getLikedJobs().contains(job)) {
	        return true;
	    }

	    candidate.getLikedJobs().add(job);
	    candidateRepository.save(candidate);

	    return true;
	}
	
	@Override
	@Transactional
	public boolean unLikedJob(String jobId, String candicateId) {
	    Job job = jobRepository.findById(jobId)
	            .orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));

	    Candidate candidate = candidateRepository.findById(candicateId)
	            .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

	    candidate.getLikedJobs().remove(job);

	    return candidateRepository.save(candidate) != null;
	}

	
	@Override
    public List<Job> getLikedJobs(){
    	String context = SecurityContextHolder.getContext().getAuthentication().getName();

		Candidate candidate = candidateRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));

		return candidate.getLikedJobs();
    }
	
	@Override
    public List<Job> getApplied(){
    	String context = SecurityContextHolder.getContext().getAuthentication().getName();

		Candidate candidate = candidateRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));
		List<Job> jobs = new ArrayList<Job>();
		for (Application app : candidate.getApplications()) {
			jobs.add(app.getJob());
		}
		
		return jobs;
    }

	@Override
	public CandidateResponse getInfor(String id) {
		Candidate c = candidateRepository.findById(id).orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));
		
		List<String> likedIds = c.getLikedJobs() == null ? Collections.emptyList()
			    : c.getLikedJobs().stream()
			        .map(Job::getJobId) 
			        .collect(Collectors.toList());
		
        List<String> appliedIds = c.getApplications() == null ? Collections.emptyList()
        	    : c.getApplications().stream()
        	        .map(app -> app.getJob())
        	        .map(Job::getJobId)
        	        .collect(Collectors.toList());

		return new CandidateResponse(
                c.getCandidateId(),
                c.getFullname(),
                c.getEmail(),
                c.getAddress(),
                c.getDateOfBirth(),
                c.getPhone(),
                c.getAvatar(),
                c.getCv(),
                c.getIsPrivate(),
                c.getRole(),
                c.getGender(),
                c.getExperience(),
                c.getTechnologies(),
                c.getSoftSkill(),
                c.getDesiredSalary(),
                likedIds,
                appliedIds
            );
	}

    @Override
    public Page<CandidateResponse> searchCandidates(CandidateSearchRequest request, Pageable pageable) {
        Specification<Candidate> spec = Specification.where(CandidateSpec.isPrivate(false))
                .and(CandidateSpec.fullname(request.getFullname()))
                .and(CandidateSpec.email(request.getEmail()))
                .and(CandidateSpec.softSkill(request.getSoftSkill()))
                .and(CandidateSpec.experience(request.getExperience()))
                .and(CandidateSpec.technologies(request.getTechnologies()))
                .and(CandidateSpec.desiredSalary(request.getDesiredSalary()));

        Page<Candidate> candidates = candidateRepository.findAll(spec, pageable);
        
        return candidates.map(c -> {
            List<String> likedIds = c.getLikedJobs() == null ? Collections.emptyList()
                    : c.getLikedJobs().stream()
                        .map(Job::getJobId)
                        .collect(Collectors.toList());

            List<String> appliedIds = c.getApplications() == null ? Collections.emptyList()
                    : c.getApplications().stream()
                        .map(app -> app.getJob())
                        .map(Job::getJobId)
                        .collect(Collectors.toList());

            return new CandidateResponse(
                    c.getCandidateId(),
                    c.getFullname(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getDateOfBirth(),
                    c.getPhone(),
                    c.getAvatar(),
                    c.getCv(),
                    c.getIsPrivate(),
                    c.getRole(),
                    c.getGender(),
                    c.getExperience(),
                    c.getTechnologies(),
                    c.getSoftSkill(),
                    c.getDesiredSalary(),
                    likedIds,
                    appliedIds
            );
        });
    }

    @Override
    public CandidateResponse getCandidateById(String id) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        return new CandidateResponse(
                c.getCandidateId(),
                c.getFullname(),
                c.getEmail(),
                c.getAddress(),
                c.getDateOfBirth(),
                c.getPhone(),
                c.getAvatar(),
                c.getIsPrivate(),
                c.getRole(),
                c.getGender(),
                c.getExperience(),
                c.getTechnologies(),
                c.getSoftSkill(),
                c.getDesiredSalary()
        );
    }

}
