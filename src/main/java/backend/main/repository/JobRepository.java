package backend.main.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.Job;
import backend.main.enums.JobStatus;

@Repository
public interface JobRepository extends CrudRepository<Job,String>, JpaSpecificationExecutor<Job> {
    Optional<Job> findByTitle(String title);
    Page<Job> findAllByStatus(JobStatus status, Pageable pageable);
    Page<Job> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Job> findByEmployer_EmployerId(String employerId, Pageable pageable);
    Page<Job> findByEmployer_EmployerIdAndStatus(String employerId, JobStatus status, Pageable pageable);
    Page<Job> findByEmployer_EmployerIdAndTitleContaining(String employerId, String keyword, Pageable pageable);
    
    // Đếm số lượng bài đăng của một employer trong khoảng thời gian
    long countByEmployer_EmployerIdAndCreatedAtBetween(String employerId, LocalDateTime start, LocalDateTime end);
}
