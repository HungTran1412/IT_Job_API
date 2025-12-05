package backend.main.services.Impl;

import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
<<<<<<< HEAD
=======
import org.springframework.data.domain.PageImpl;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import backend.main.configuration.JwtUtils;
=======
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
import jakarta.transaction.Transactional;

=======
import backend.main.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private EmployerRepository employerRepository;

<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
                .location(jobRequest.getLocation())
=======
                .location(employer.getCity())
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
                job.getDeadline().atStartOfDay(),
                job.getStatus(),
                employer.getEmployerId(),
                employer.getCompanyName(),
                employer.getLogo());
=======
                job.getDeadline(),
                job.getStatus(),
                employer.getEmployerId(),
                employer.getCompanyName());
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    }

    @Override
    @Transactional
<<<<<<< HEAD
    public Page<JobResponse> findAll()  {
        Page<Job> jobs = (Page<Job>) jobRepository.findAll();
=======
    public Page<JobResponse> findAll(Pageable pageable)  {
    	List<Job> results = (List<Job>) jobRepository.findAll();
        Page<Job> jobs = new PageImpl<Job>(results,pageable,results.size());
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
                job.getDeadline().atStartOfDay(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo()
=======
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName()
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
        ));
    }

    @Override
    @Transactional
<<<<<<< HEAD
    public Optional<Job> findById(String jobId) {
        return jobRepository.findById(jobId);
=======
    public Job findById(String jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    }

    @Override
    @Transactional
    public Optional<Job> findByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    @Override
    @Transactional
<<<<<<< HEAD
    public void deleteById(String jobId) {
        jobRepository.deleteById(jobId);
=======
    public void deleteAllById(List<String> jobId) {
        jobRepository.deleteAllById(jobId);
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
                job.getDeadline().atStartOfDay(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo());
=======
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName());
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
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
<<<<<<< HEAD
                job.getDeadline().atStartOfDay(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName(),
                job.getEmployer().getLogo()
=======
                job.getDeadline(),
                job.getStatus(),
                job.getEmployer().getEmployerId(),
                job.getEmployer().getCompanyName()
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
        ));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
<<<<<<< HEAD
    public JobStatus reviewJob(JobReviewRequest request) {
        Job j =  jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));

        j.setStatus(request.getJobStatus());

        jobRepository.save(j);

        return  j.getStatus();
    }

=======
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

    @Override
    @Transactional
    public Page<JobResponse> findJobsByEmployer(String employerId, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByEmployer_EmployerId(employerId, pageable);
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
                job.getEmployer().getCompanyName()
        ));
    }

    @Override
    @Transactional
    public Page<JobResponse> findJobsByEmployerAndStatus(String employerId, JobStatus status, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByEmployer_EmployerIdAndStatus(employerId, status, pageable);
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
                job.getEmployer().getCompanyName()
        ));
    }

    @Override
    @Transactional
    public Page<JobResponse> searchJobsByEmployer(String employerId, String keyword, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByEmployer_EmployerIdAndTitleContaining(employerId, keyword, pageable);
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
                job.getEmployer().getCompanyName()
        ));
    }

	@Override
    @Transactional
	public JobResponse getJob(String id) {
		Job job = jobRepository.findById(id).orElseThrow((() -> new AppException(Code.JOB_NOT_FOUND)));
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
                job.getEmployer().getCompanyName());
	}

>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
}
