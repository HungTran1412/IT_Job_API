package backend.main.services.Impl;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
=======
import lombok.extern.slf4j.Slf4j;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.enums.ApplicationStatus;
import backend.main.repository.ApplicationRepository;
import backend.main.services.ApplicationService;
import jakarta.transaction.Transactional;

<<<<<<< HEAD
=======
@Slf4j
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    public Optional<Application> findById(String applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    public List<Application> findByCandidate(Candidate candidate) {
        return applicationRepository.findByCandidate(candidate);
    }

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    public void delete(String applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}
