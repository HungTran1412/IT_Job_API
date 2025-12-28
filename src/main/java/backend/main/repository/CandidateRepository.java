package backend.main.repository;

import backend.main.entities.Candidate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CandidateRepository extends CrudRepository<Candidate, String>, JpaSpecificationExecutor<Candidate> {
    Optional<Candidate> findByEmail(String email);
}
