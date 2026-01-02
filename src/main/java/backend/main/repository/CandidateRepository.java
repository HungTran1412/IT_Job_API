package backend.main.repository;

import backend.main.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, String>, JpaSpecificationExecutor<Candidate> {
    Optional<Candidate> findByEmail(String email);
}
