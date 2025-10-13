package backend.main.configuration;

import backend.main.entities.VerificationToken;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.repository.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    VerificationTokenRepository verificationTokenRepository;
    CandidateRepository candidateRepository;
    EmployerRepository employerRepository;

    @Scheduled(fixedRate = 60000) // Mỗi 1 phút kiểm tra
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<VerificationToken> expiredTokens = verificationTokenRepository.findAllByExpirationTimeBefore(now);

        for (VerificationToken token : expiredTokens) {
            // Xóa token
            verificationTokenRepository.delete(token);

            // Xử lý Candidate nếu có
            if (token.getCandidate() != null && !token.getCandidate().getEnabled()) {
                candidateRepository.delete(token.getCandidate());
            }

            // Xử lý Employer nếu có
            if (token.getEmployer() != null && !token.getEmployer().getEnabled()) {
                employerRepository.delete(token.getEmployer());
            }
        }
    }
}
