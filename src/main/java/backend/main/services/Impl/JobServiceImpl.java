package backend.main.services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Application;
import backend.main.entities.Employer;
import backend.main.entities.Job;
import backend.main.enums.Code;
import backend.main.enums.JobStatus;
import backend.main.exception.AppException;
import backend.main.repository.EmployerRepository;
import backend.main.repository.JobRepository;
import backend.main.services.JobService;
import backend.main.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public JobResponse save(JobRequest jobRequest) {
        String context = SecurityContextHolder.getContext().getAuthentication().getName();

        Employer employer = employerRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));

        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .salaryMin(jobRequest.getSalaryMin())
                .salaryMax(jobRequest.getSalaryMax())
                .position(jobRequest.getPosition())
                .workingFrom(jobRequest.getWorkingFrom())
                .location(jobRequest.getLocation())
                .technologies(jobRequest.getTechnologies())
                .deadline(jobRequest.getDeadline())
                        .build();

        job.setEmployer(employer);
        job.setApplications(new ArrayList<Application>());
        jobRepository.save(job);

        return new JobResponse(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getPosition(),
                job.getTechnologies(),
                job.getWorkingFrom(),
                job.getLocation(),
                job.getDeadline(),
                job.getStatus(),
                employer.getEmployerId(),
                employer.getCompanyName(),
                employer.getLogo());
    }

    @Override
    @Transactional
    public Page<JobResponse> findAll(Pageable pageable)  {
    	List<Job> results = (List<Job>) jobRepository.findAll();
        Page<Job> jobs = new PageImpl<Job>(results,pageable,results.size());
        return jobs.map(job -> new JobResponse(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getPosition(),
                job.getTechnologies(),
                job.getWorkingFrom(),
                job.getLocation(),
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo()
        ));
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
    public void deleteAllById(List<String> jobId) {
        jobRepository.deleteAllById(jobId);
    }

    @Override
    @Transactional
    public Page<Job> search(String keyword, String location, String salaryRange, Pageable pageable) {
        return jobRepository.findAllByTitleContaining(keyword, pageable);
    }


    @Override
    @Transactional
    public JobResponse updateJob(String jobId, JobRequest jobRequest) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));
        
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setSalaryMin(jobRequest.getSalaryMin());
        job.setSalaryMax(jobRequest.getSalaryMax());
        job.setPosition(jobRequest.getPosition());
        job.setTechnologies(jobRequest.getTechnologies());
        job.setWorkingFrom(jobRequest.getWorkingFrom());
        job.setLocation(jobRequest.getLocation());
        job.setDeadline(jobRequest.getDeadline());
        
        job = jobRepository.save(job);
        
        return new JobResponse(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getPosition(),
                job.getTechnologies(),
                job.getWorkingFrom(),
                job.getLocation(),
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo());
    }

	@Override
    @Transactional
	public Page<Job> findAllByStatus(JobStatus status, Pageable pageable) {

		return jobRepository.findAllByStatus(status, pageable);
	}

    @Override
    @Transactional
    public Page<JobResponse> findAllByStatusApproved(Pageable pageable) {
        Page<Job> jobs = jobRepository.findAllByStatus(JobStatus.APPROVED, pageable);
        return jobs.map(job -> new JobResponse(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getPosition(),
                job.getTechnologies(),
                job.getWorkingFrom(),
                job.getLocation(),
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo()
        ));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public boolean reviewJob(JobReviewRequest request) {
        try {
			List<Job> jobs = (List<Job>) jobRepository.findAllById(request.getJobId());

			if (jobs.size() != request.getJobId().size()) {
			    throw new AppException(Code.JOB_NOT_FOUND);
			}

			jobs.forEach(job -> job.setStatus(request.getJobStatus()));
			jobRepository.saveAll(jobs);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

        return false;
    }

}
