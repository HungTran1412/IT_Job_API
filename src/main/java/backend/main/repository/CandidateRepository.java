package backend.main.repository;

import backend.main.entities.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CandidateRepository extends CrudRepository<Candidate, String> {
    Optional<Candidate> findByEmail(String email);
}
