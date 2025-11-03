package backend.main.repository;

import backend.main.entities.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin,Integer> {
    Optional<Admin> findByEmail(String email);
}
