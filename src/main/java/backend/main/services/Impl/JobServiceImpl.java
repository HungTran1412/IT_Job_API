package backend.main.services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.entities.Application;
import backend.main.entities.Employer;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.enums.JobStatus;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.repository.JobRepository;
import backend.main.services.JobService;
import jakarta.transaction.Transactional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public Job save(JobRequest jobRequest) {
        String context = SecurityContextHolder.getContext().getAuthentication().getName();

        Employer employer = employerRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));

        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .requirements(jobRequest.getRequirements())
                .salary(jobRequest.getSalary())
                .location(jobRequest.getLocation())
                .deadline(jobRequest.getDeadline())
                        .build();

        job.setEmployer(employer);
        job.setApplications(new ArrayList<Application>());

        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public List<Job> findAll() {
        return (List<Job>) jobRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Job> findById(String jobId) {
        return jobRepository.findById(jobId);
    }

    @Override
    @Transactional
    public Optional<Job> findByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public void deleteById(String jobId) {
        jobRepository.deleteById(jobId);
    }

    @Override
    @Transactional
    public List<Job> search(String keyword, String location, String salaryRange) {
        System.out.println(jobRepository.findAllByTitleContaining(keyword).toArray().toString());
        return jobRepository.findAllByTitleContaining(keyword);
    }


    @Override
    @Transactional
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

	@Override
    @Transactional
	public List<Job> findAllByStatus(JobStatus status) {

		return jobRepository.findAllByStatus(status);
	}

    @Override
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public JobStatus reviewJob(JobReviewRequest request) {
        Job j =  jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));

        j.setStatus(request.getJobStatus());

        jobRepository.save(j);

        return  j.getStatus();
    }

}
