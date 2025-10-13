package backend.main.configuration;

import backend.main.repository.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupScheduler {
    VerificationTokenRepository verificationTokenRepository;

    public TokenCleanupScheduler(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanExpiredTokens() {
        verificationTokenRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}
