package backend.main.repository;

import backend.main.entities.Employer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployerRepository extends CrudRepository<Employer, String> {
    Optional<Employer> findByEmail(String email);
}
