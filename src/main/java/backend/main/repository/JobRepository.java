package backend.main.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.Job;
import backend.main.enums.JobStatus;

@Repository
public interface JobRepository extends CrudRepository<Job,String> {
    Optional<Job> findByTitle(String title);
    Page<Job> findAllByStatus(JobStatus status, Pageable pageable);
    Page<Job> findAllByTitleContaining(String keyword, Pageable pageable);
}
