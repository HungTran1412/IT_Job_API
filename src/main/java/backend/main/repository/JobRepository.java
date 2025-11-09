package backend.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.Job;
import backend.main.enums.JobStatus;

@Repository
public interface JobRepository extends CrudRepository<Job,String> {
    Optional<Job> findByTitle(String title);
    List<Job> findAllByStatus(JobStatus status);
    List<Job> findAllByTitleContaining(String keyword);
}
