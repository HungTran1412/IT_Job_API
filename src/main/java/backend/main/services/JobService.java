package backend.main.services;

import java.util.List;
import java.util.Optional;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.entities.Job;
import backend.main.enums.JobStatus;

public interface JobService {
    Job save(JobRequest jobRequest);
    List<Job> findAll();
    Optional<Job> findById(String jobId);
    Optional<Job> findByTitle(String title);
    void deleteById(String jobId);
    Job updateJob(Job job);
    List<Job> search(String keyword, String location, String salaryRange);
    List<Job> findAllByStatus(JobStatus status);
    JobStatus reviewJob(JobReviewRequest request);
}
