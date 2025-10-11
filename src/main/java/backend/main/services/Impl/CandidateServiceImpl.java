package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.dto.response.CandidateResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import backend.main.utils.CloudinaryImageUpload;
import backend.main.utils.SendEmailHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

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
    CloudinaryImageUpload cloudinaryImageUpload;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, SendEmailHandler sendEmailHandler, CloudinaryImageUpload cloudinaryImageUpload) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.sendEmailHandler = sendEmailHandler;
        this.cloudinaryImageUpload = cloudinaryImageUpload;
    }

    private String generateCandidateID() {
        return "USER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Candidate register(CandidateRequest candidateRequest) {
        if(candidateRepository.findByEmail(candidateRequest.getEmail()).isPresent()){
            throw new AppException(Code.EMAIL_EXISTED);
        }
        // Tạo đối tượng Candidate mới
        Candidate candidate = new Candidate();

        // Gán giá trị từ request sang entity
        candidate.setCandidateId(generateCandidateID());
        candidate.setFullname(candidateRequest.getFullname());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRequest.getPassword())); // Mã hóa mật khẩu
        candidate.setCreateAt(LocalDate.now()); // Ngày tạo tài khoản
        candidate.setUpdateAt(LocalDate.now()); // Ngày cập nhật tài khoản
        candidate.setRole(Role.ROLE_CANDIDATE); // Gán vai trò mặc định
        candidate.setEnabled(false);

        //sinh token ngau nhien
        String token = UUID.randomUUID().toString();
        candidate.setVerificationToken(token);

        //gan link + token vao email va gui
        String verifyLink = "http://localhost:8080/user/verify?token=" + token;
        sendEmailHandler.sendVerificationEmail(candidate.getEmail(), verifyLink);

        //luu nguoi dung
        return saveCandidate(candidate);
    }

    @Override
    public Candidate verifyCandidate(String token){
        Candidate cd = candidateRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        cd.setUpdateAt(LocalDate.now());
        cd.setEnabled(true);
        cd.setVerificationToken(null);
        return saveCandidate(cd);
    }

    @Override
    public CandidateResponse getCandidateById(String id) {
        Candidate c = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.CANDIDATE_NOT_FOUND));

        return new CandidateResponse(
                c.getFullname(),
                c.getEmail(),
                c.getAddress(),
                c.getDateOfBirth(),
                c.getPhone(),
                c.getAvatar(),
                c.getCv());
    }

    @Override
    public CandidateLoginResponse login(CandidateLoginRequest candidateLoginRequest) {
        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Candidate c = candidateRepository.findByEmail(candidateLoginRequest.getEmail())
                .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(candidateLoginRequest.getPassword(), c.getPassword())) {
            throw new AppException(Code.WRONG_PASSWORD);
        }

        if(c.getEnabled() == false){
            throw new AppException(Code.ACCOUNT_UNENABLED);
        }

        String token = jwtUtils.generateToken(c.getEmail(), c.getRole());

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return new CandidateLoginResponse(
                c.getCandidateId(),
                c.getRole(),
                token
        );
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
        c.setUpdateAt(LocalDate.now());
        c.setCv(request.getCv());

        //Kiem tra xem nguoi dung co cap nhat anh khong
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()){
            System.out.println("Image: " + request.getAvatar().getOriginalFilename());
            String imgUrl = cloudinaryImageUpload.uploadImage(request.getAvatar());
            c.setAvatar(imgUrl);
        }

        return candidateRepository.save(c);
    }

    private Candidate saveCandidate(Candidate c) {
        c.setUpdateAt(LocalDate.now());
        return candidateRepository.save(c);
    }
}
