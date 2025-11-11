package backend.main.services;

import java.util.List;
import java.util.Optional;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Job;
import backend.main.enums.JobStatus;

public interface JobService {
    JobResponse save(JobRequest jobRequest);
    List<JobResponse> findAll();
    Optional<Job> findById(String jobId);
    Optional<Job> findByTitle(String title);
    void deleteById(String jobId);
    JobResponse updateJob(String jobId, JobRequest jobRequest);
    List<Job> search(String keyword, String location, String salaryRange);
    List<Job> findAllByStatus(JobStatus status);
    List<JobResponse> findAllByStatusApproved();
    JobStatus reviewJob(JobReviewRequest request);

}
