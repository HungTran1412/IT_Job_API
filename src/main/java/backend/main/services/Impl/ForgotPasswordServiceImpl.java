package backend.main.services.Impl;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.entities.VerificationToken;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.repository.VerificationTokenRepository;
import backend.main.services.ForgotPasswordService;
import backend.main.utils.SendEmailHandler;
import backend.main.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    SendEmailHandler sendEmailHandler;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void sendOtp(ForgotPasswordRequest request) {
        String email = request.getEmail();
        Candidate candidate = candidateRepository.findByEmail(email).orElse(null);
        Employer employer = employerRepository.findByEmail(email).orElse(null);

        if (candidate == null && employer == null) {
            // Không làm gì cả để bảo mật
            return;
        }

        // Tạo mã OTP gồm 6 chữ số
        String token = String.format("%06d", new Random().nextInt(1000000));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút

        if (candidate != null) {
            verificationToken.setCandidate(candidate);
        } else {
            verificationToken.setEmployer(employer);
        }

        verificationTokenRepository.save(verificationToken);

        // Gọi đến SendEmailHandler để gửi email OTP
        sendEmailHandler.sendOTPEmail(email, token);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new AppException(Code.TOKEN_EXPIRED);
        }

        Candidate candidate = verificationToken.getCandidate();
        Employer employer = verificationToken.getEmployer();

        String newPassword = request.getNewPassword();
        ValidationUtils.validatePassword(newPassword);
        String newPasswordEncoded = passwordEncoder.encode(newPassword);

        if (candidate != null) {
            candidate.setPassword(newPasswordEncoded);
            candidateRepository.save(candidate);
        } else if (employer != null) {
            employer.setPassword(newPasswordEncoded);
            employerRepository.save(employer);
        } else {
            throw new AppException(Code.USER_NOT_FOUND);
        }

        verificationTokenRepository.delete(verificationToken);
    }
}
