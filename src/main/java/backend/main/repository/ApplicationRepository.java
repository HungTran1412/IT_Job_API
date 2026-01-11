package backend.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    List<Application> findByCandidate(Candidate candidate);
    List<Application> findByJob(Job job);
    Application findByJobAndCandidate(Job job, Candidate candidate);
    @Query("""
    		   select a from Application a
    		   where a.job.jobId = :jobId
    		""")
    		Page<Application> findByJobId(@Param("jobId") String jobId, Pageable pageable);

    Page<Application> findByJob_JobIdIn(List<String> jobIds, Pageable pageable);

}
