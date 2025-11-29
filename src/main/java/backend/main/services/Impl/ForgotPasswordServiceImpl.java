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
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final EmployerRepository employerRepository;
    private final CandidateRepository candidateRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendOtp(ForgotPasswordRequest request) {
        String email = request.getEmail();
        Candidate candidate = candidateRepository.findByEmail(email).orElse(null);
        Employer employer = employerRepository.findByEmail(email).orElse(null);

        if (candidate == null && employer == null) {
            // Không làm gì cả để bảo mật
            return;
        }

        String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // Tạo OTP 6 ký tự chữ hoa
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút

        if (candidate != null) {
            verificationToken.setCandidate(candidate);
        } else {
            verificationToken.setEmployer(employer);
        }

        verificationTokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Yêu cầu đặt lại mật khẩu");
        message.setText("Mã OTP để đặt lại mật khẩu của bạn là: " + token + ". Mã này sẽ hết hạn sau 5 phút.");
        mailSender.send(message);
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

        String newPasswordEncoded = passwordEncoder.encode(request.getNewPassword());

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
