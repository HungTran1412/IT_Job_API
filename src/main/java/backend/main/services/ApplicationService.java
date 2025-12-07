package backend.main.services;

import java.util.List;
import java.util.Optional;

import backend.main.dto.request.application.ApplicationRequest;
import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.enums.ApplicationStatus;

public interface ApplicationService {
    Application save(ApplicationRequest request);
    List<Application> findAll();
    Optional<Application> findById(String applicationId);
    List<Application> findByCandidate(Candidate candidate);
    List<Application> findByJob(Job job);
    void updateStatus(String applicationId, ApplicationStatus status);
    void delete(String applicationId);
}
