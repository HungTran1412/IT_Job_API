package backend.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    List<Application> findByCandidate(Candidate candidate);
    List<Application> findByJob(Job job);
    Application findByJobAndCandidate(Job job, Candidate candidate);
}
