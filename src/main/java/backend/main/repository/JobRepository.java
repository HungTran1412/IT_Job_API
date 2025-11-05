package backend.main.repository;

import backend.main.entities.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JobRepository extends CrudRepository<Job,String> {
    Optional<Job> findByTilte(String tilte);

}
