package backend.main.services.Impl;

import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.utils.JwtUtils;
import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.entities.Admin;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.AdminRepository;
import backend.main.services.AdminService;
import backend.main.utils.ValidationUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    EmployerRepository employerRepository;

    @Override
    @Transactional
    public String login(LoginRequest request) {
        ValidationUtils.validateEmail(request.getEmail());

        // Tìm ứng viên theo email, nếu không có thì ném lỗi
        Admin c = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

        // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB
        if (!passwordEncoder.matches(request.getPassword(), c.getPassword())) {
            throw new AppException(Code.WRONG_PASSWORD);
        }

        // Trả về thông tin ứng viên (không bao gồm mật khẩu)
        return jwtUtils.generateToken(String.valueOf(c.getId()), c.getEmail(), c.getRole(), request.isRememberMe());
    }

    @Transactional
    @Override
    public boolean changePassword(String email, ChangePasswordRequest request) {
        try {
            Admin c = adminRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(Code.EMAIL_DOES_NOT_EXIST));

            if(c.getPassword() == null){
                throw new AppException(Code.PASSWORD_IS_NULL);
            }

            //kiểm tra mật khẩu cũ trước khi đổi
            if(!passwordEncoder.matches(request.getOldPassword(), c.getPassword())){
                throw new AppException(Code.OLD_PASSWORD_NOT_MATCH);
            }

            c.setPassword(passwordEncoder.encode(request.getNewPassword()));
            adminRepository.save(c);
            return true;
        } catch (AppException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void updateUserLockStatus(String userId, boolean isLocked) {
        // Thử tìm trong Candidate
        Optional<Candidate> candidateOpt = candidateRepository.findById(userId);
        if (candidateOpt.isPresent()) {
            Candidate candidate = candidateOpt.get();
            candidate.setIsLocked(isLocked);
            candidateRepository.save(candidate);
            return;
        }

        // Thử tìm trong Employer
        Optional<Employer> employerOpt = employerRepository.findById(userId);
        if (employerOpt.isPresent()) {
            Employer employer = employerOpt.get();
            employer.setIsLocked(isLocked);
            employerRepository.save(employer);
            return;
        }

        throw new AppException(Code.USER_NOT_FOUND);
    }
}
