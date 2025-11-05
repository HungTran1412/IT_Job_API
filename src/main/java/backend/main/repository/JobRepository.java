package backend.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import backend.main.entities.Job;

public interface JobRepository extends CrudRepository<Job,String> {
    Optional<Job> findByTitle(String title);
    List<Job> findByStatus(String status);
    List<Job> findByTitleContainingAndLocationContaining(String keyword, String location);

}
