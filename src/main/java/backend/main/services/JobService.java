package backend.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.request.job.JobSearchRequest;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Job;
import backend.main.enums.JobStatus;

public interface JobService {
    JobResponse save(JobRequest jobRequest, String email);
    JobResponse getJob(String id);
    Page<JobResponse> findAll(Pageable pageable );
    Job findById(String jobId);
    Optional<Job> findByTitle(String title);
    void deleteAllById(List<String> jobId);
    JobResponse updateJob(String jobId, JobRequest jobRequest);
    Page<Job> search(JobSearchRequest request, Pageable pageable);
    Page<Job> findAllByStatus(JobStatus status, Pageable pageable);
    Page<JobResponse> findAllByStatusApproved(Pageable pageable);
    Page<JobResponse> findAllByStatusPending(Pageable pageable);
    boolean reviewJob(JobReviewRequest request);
    Page<JobResponse> findJobsByEmployer(String employerId, Pageable pageable);
    Page<JobResponse> findJobsByEmployerAndStatus(String employerId, JobStatus status, Pageable pageable);
    Page<JobResponse> searchJobsByEmployer(String employerId, String keyword, Pageable pageable);
}
