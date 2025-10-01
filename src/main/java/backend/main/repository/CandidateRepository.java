package backend.main.repository;

import backend.main.entities.Candidate;
import org.springframework.data.repository.CrudRepository;

public interface CandidateRepository extends CrudRepository<Candidate, String> {
}
