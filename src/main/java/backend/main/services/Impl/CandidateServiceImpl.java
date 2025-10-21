package backend.main.services.Impl;

import backend.main.configuration.AppProperties;
import backend.main.configuration.JwtUtils;
import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateLoginRequest;
import backend.main.entities.Candidate;
import backend.main.entities.VerificationToken;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.VerificationTokenRepository;
import backend.main.services.CandidateService;
import backend.main.utils.CloudinaryFileUpload;
import backend.main.utils.SendEmailHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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

    private String generateCandidateID() {
        return "USER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Candidate register(CandidateRegisterRequest candidateRequest) {
        if(candidateRepository.findByEmail(candidateRequest.getEmail()).isPresent()){
            throw new AppException(Code.EMAIL_EXISTED);
        }
        // Tạo đối tượng Candidate mới
        Candidate candidate = new Candidate();

        // Gán giá trị từ request sang entity
        candidate.setCandidateId(generateCandidateID());
        candidate.setFullname(candidateRequest.getFullName());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRequest.getPassword())); // Mã hóa mật khẩu
        candidate.setCreateAt(LocalDateTime.now()); // Ngày tạo tài khoản
        candidate.setUpdateAt(LocalDateTime.now()); // Ngày cập nhật tài khoản
        candidate.setRole(Role.ROLE_CANDIDATE); // Gán vai trò mặc định
        candidate.setEnabled(false);

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
        newToken.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        verificationTokenRepository.save(newToken);

        String verifyLink = appProperties.getBaseUrl() + appProperties.getVerify().getCandidate() + token;
        sendEmailHandler.sendVerificationEmail(candidate.getEmail(), verifyLink);
    }


    @Override
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
        return saveCandidate(cd);
    }

    @Override
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
    public String login(CandidateLoginRequest request) {
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

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return jwtUtils.generateToken(c.getCandidateId(),c.getEmail(), c.getRole());
    }

    @Override
    public Candidate updateInfo(String id, CandidateRequest request) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        c.setFullname(request.getFullname());
        c.setGender(request.getGender());
        c.setPhone(request.getPhone());
        c.setDateOfBirth(request.getDateOfBirth());
        c.setAddress(request.getAddress());
        c.setUpdateAt(LocalDateTime.now());

        //Kiem tra xem nguoi dung co cap nhat anh khong
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()){
            System.out.println("Image: " + request.getAvatar().getOriginalFilename());
            String imgUrl = cloudinaryFileUpload.uploadImage(request.getAvatar());
            c.setAvatar(imgUrl);
        }

        //Kiểm tra người dùng có thêm cv không
        if(request.getCv() != null && !request.getCv().isEmpty()){
            System.out.println("CV: " + request.getCv().getOriginalFilename());
            String url = cloudinaryFileUpload.uploadCv(request.getCv());
            c.setCv(url);
        }

        return saveCandidate(c);
    }

    private Candidate saveCandidate(Candidate c) {
        c.setUpdateAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }
}
