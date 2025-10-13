package backend.main.repository;

import backend.main.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);
    List<VerificationToken> findAllByExpirationTimeBefore(LocalDateTime now);
}
