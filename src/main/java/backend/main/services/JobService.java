package backend.main.services;

import java.util.List;
import java.util.Optional;

import backend.main.entities.Job;
import backend.main.enums.JobStatus;

public interface JobService {
    Job save(Job job);
    List<Job> findAll();
    Optional<Job> findById(String jobId);
    Optional<Job> findByTitle(String title);
    void deleteById(String jobId);
    Job updateJob(Job job);
    List<Job> search(String keyword, String location, String salaryRange);
    List<Job> findByStatus(JobStatus status);
}
