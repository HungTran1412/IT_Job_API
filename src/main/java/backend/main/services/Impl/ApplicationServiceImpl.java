package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.enums.ApplicationStatus;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.repository.ApplicationRepository;
import backend.main.repository.CandidateRepository;
import backend.main.repository.JobRepository;
import backend.main.services.ApplicationService;
import backend.main.utils.CloudinaryFileUpload;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ApplicationServiceImpl implements ApplicationService {

    
    ApplicationRepository applicationRepository;
    JobRepository jobRepository;
    CandidateRepository candidateRepository;
    CloudinaryFileUpload cloudinaryFileUpload;

    @Override
    @Transactional
    public Application save(String name, String phone, String email, MultipartFile cv, String jobId) {
    	
    	try {
			Job job = jobRepository.findById(jobId).orElseThrow((() -> new AppException(Code.JOB_NOT_FOUND)));
			
			String context = SecurityContextHolder.getContext().getAuthentication().getName();

			Candidate candidate = candidateRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));
			
			String cvString = cloudinaryFileUpload.uploadCv(cv);
			
			Application application = Application.builder()
					.appliedDate(LocalDateTime.now())
					.name(name)
					.email(email)
					.phone(phone)
					.cv(cvString)
					.candidate(candidate)
					.job(job)
					.build();
			
			return applicationRepository.save(application);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
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