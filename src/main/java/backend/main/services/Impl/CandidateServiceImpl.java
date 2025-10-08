package backend.main.services.Impl;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.entities.Candidate;
import backend.main.enums.ErrorCode;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import backend.main.utils.SendEmailHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class CandidateServiceImpl implements CandidateService {
    CandidateRepository candidateRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    SendEmailHandler sendEmailHandler;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, SendEmailHandler sendEmailHandler) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.sendEmailHandler = sendEmailHandler;
    }

    private String generateCandidateID() {
        return "USER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Candidate register(CandidateRequest candidateRequest) {
        if(candidateRepository.findByEmail(candidateRequest.getEmail()).isPresent()){
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        // Tạo đối tượng Candidate mới
        Candidate candidate = new Candidate();

        // Gán giá trị từ request sang entity
        candidate.setCandidateId(generateCandidateID());
        candidate.setEmail(candidateRequest.getEmail());
        candidate.setPassword(passwordEncoder.encode(candidateRequest.getPassword())); // Mã hóa mật khẩu
        candidate.setCreateAt(LocalDate.now()); // Ngày tạo tài khoản
        candidate.setUpdateAt(LocalDate.now()); // Ngày cập nhật tài khoản
        candidate.setRole(Role.ROLE_CANDIDATE); // Gán vai trò mặc định
        candidate.setEnabled(false);

        String token = UUID.randomUUID().toString();
        candidate.setVerificationToken(token);

        String verifyLink = "http://localhost:8080/user/verify?token=" + token;
        sendEmailHandler.sendVerificationEmail(candidate.getEmail(), verifyLink);

        return  candidateRepository.save(candidate);
    }

    @Override
    public Candidate verifyCandidate(String token){
        Candidate cd = candidateRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_INVALID));

        cd.setUpdateAt(LocalDate.now());
        cd.setEnabled(true);
        cd.setVerificationToken(null);
        return candidateRepository.save(cd);
    }

    @Override
    public CandidateLoginResponse login(CandidateLoginRequest candidateLoginRequest) {
        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Candidate candidate = candidateRepository.findByEmail(candidateLoginRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_DOES_NOT_EXIST));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(candidateLoginRequest.getPassword(), candidate.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        if(candidate.getEnabled() == false){
            throw new AppException(ErrorCode.ACCOUNT_UNENABLED);
        }

        String token = jwtUtils.generateToken(candidate.getEmail(), candidate.getRole());

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return new CandidateLoginResponse(
                candidate.getCandidateId(),
                candidate.getFullname(),
                candidate.getEmail(),
                candidate.getGender(),
                candidate.getAddress(),
                candidate.getDateOfBirth(),
                candidate.getCreateAt(),
                candidate.getUpdateAt(),
                candidate.getPhone(),
                candidate.getAvatar(),
                candidate.getRole(),
                token
        );
    }

    @Override
    public Candidate updateInfo(String id, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_FOUND));

        candidate.setFullname(request.getFullname());
        candidate.setGender(request.getGender());
        candidate.setPhone(request.getPhone());
        candidate.setDateOfBirth(request.getDateOfBirth());
        candidate.setAddress(request.getAddress());
        candidate.setAvatar(request.getAvatar());
        candidate.setUpdateAt(LocalDate.now());
        candidate.setCv(request.getCv());

        return candidateRepository.save(candidate);
    }
}
