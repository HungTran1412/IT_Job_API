package backend.main.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Job;
import backend.main.enums.JobStatus;

public interface JobService {
    JobResponse save(JobRequest jobRequest);
    Page<JobResponse> findAll( );
    Optional<Job> findById(String jobId);
    Optional<Job> findByTitle(String title);
    void deleteById(String jobId);
    JobResponse updateJob(String jobId, JobRequest jobRequest);
    Page<Job> search(String keyword, String location, String salaryRange, Pageable pageable);
    Page<Job> findAllByStatus(JobStatus status, Pageable pageable);
    Page<JobResponse> findAllByStatusApproved(Pageable pageable);
    JobStatus reviewJob(JobReviewRequest request);

}
