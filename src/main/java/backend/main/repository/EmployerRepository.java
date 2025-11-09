package backend.main.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import backend.main.entities.Employer;

public interface EmployerRepository extends CrudRepository<Employer, String> {
    Optional<Employer> findByEmail(String email);
}
