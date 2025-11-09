package backend.main.repository;

import java.util.List;
import java.util.Optional;

import backend.main.enums.JobStatus;
import org.springframework.data.repository.CrudRepository;

import backend.main.entities.Job;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends CrudRepository<Job,String> {
    Optional<Job> findByTitle(String title);
    List<Job> findAllByStatus(JobStatus status);
    List<Job> findByTitleContainingAndLocationContaining(String keyword, String location);

}
