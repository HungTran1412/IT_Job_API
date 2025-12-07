package backend.main.services.Impl;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;
import backend.main.dto.request.VerifyOtpRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    @Override
    public void sendOtp(ForgotPasswordRequest request) {
        String email = request.getEmail();
        Candidate candidate = candidateRepository.findByEmail(email).orElse(null);
        Employer employer = employerRepository.findByEmail(email).orElse(null);

        if (candidate == null && employer == null) {
            return;
        }

        String token = String.format("%06d", new Random().nextInt(1000000));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationToken.setVerified(false); // Đặt trạng thái chưa xác thực

        if (candidate != null) {
            verificationToken.setCandidate(candidate);
        } else {
            verificationToken.setEmployer(employer);
        }

        verificationTokenRepository.save(verificationToken);
        sendEmailHandler.sendOTPEmail(email, token);
    }

    @Transactional
    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(request.getOtp())
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new AppException(Code.TOKEN_EXPIRED);
        }

        verificationToken.setVerified(true);
        verificationTokenRepository.save(verificationToken);
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(request.getOtp())
                .orElseThrow(() -> new AppException(Code.TOKEN_INVALID));

        if (!verificationToken.isVerified()) {
            throw new AppException(Code.TOKEN_NOT_VERIFIED);
        }

        Candidate candidate = verificationToken.getCandidate();
        Employer employer = verificationToken.getEmployer();
        String newPassword = request.getNewPassword();

        log.info("Đang kiểm tra mật khẩu nhận được: '{}'", newPassword);
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
