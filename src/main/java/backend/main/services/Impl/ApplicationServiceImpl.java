package backend.main.services.Impl;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.enums.ApplicationStatus;
import backend.main.repository.ApplicationRepository;
import backend.main.services.ApplicationService;
import jakarta.transaction.Transactional;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Application> findById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    @Transactional
    public List<Application> findByCandidate(Candidate candidate) {
        return applicationRepository.findByCandidate(candidate);
    }

    @Override
    @Transactional
    public List<Application> findByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    @Override
    @Transactional
    public void updateStatus(String applicationId, ApplicationStatus status) {
        Optional<Application> applicationOptional = findById(applicationId);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus(status);
            applicationRepository.save(application);
        }
    }

    @Override
    @Transactional
    public void delete(String applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}