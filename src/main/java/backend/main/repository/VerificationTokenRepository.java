package backend.main.repository;

import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByCandidate(Candidate candidate);

    Optional<VerificationToken> findByEmployer(Employer employer);

    List<VerificationToken> findAllByExpirationTimeBefore(LocalDateTime now);
}
