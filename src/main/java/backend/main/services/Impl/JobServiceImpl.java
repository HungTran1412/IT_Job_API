package backend.main.services.Impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import backend.main.dto.request.job.JobRequest;
import backend.main.dto.request.job.JobReviewRequest;
import backend.main.dto.request.job.JobSearchRequest;
import backend.main.dto.response.JobResponse;
import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.entities.EmployerSubscription;
import backend.main.entities.Job;
import backend.main.entities.Notification;
import backend.main.entities.VipPackage;
import backend.main.enums.Code;
import backend.main.enums.JobStatus;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.CandidateRepository;
import backend.main.repository.EmployerRepository;
import backend.main.repository.EmployerSubscriptionRepository;
import backend.main.repository.JobRepository;
import backend.main.repository.NotificationRepository;
import backend.main.services.JobService;
import backend.main.specification.JobSpec;
import backend.main.utils.JwtUtils;
import backend.main.utils.SseUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private SseUtils sseUtils;
    
    @Autowired
    private EmployerSubscriptionRepository employerSubscriptionRepository;

    @Transactional
    @Override
    public JobResponse save(JobRequest jobRequest, String email) {
//        String context = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("Context: " + context);
        Employer employer = employerRepository.findByEmail(email).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));

        // Kiểm tra giới hạn bài đăng theo gói VIP
        checkJobPostLimit(employer);

        Job job = null;
        
        if(!jobRequest.isCheckSalary()) {
        	job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .salaryMin(jobRequest.getSalaryMin())
                .salaryMax(jobRequest.getSalaryMax())
                .position(jobRequest.getPosition())
                .workingFrom(jobRequest.getWorkingFrom())
                .location(employer.getCity())
                .technologies(jobRequest.getTechnologies())
                .deadline(jobRequest.getDeadline())
                .logo(employer.getLogo())
                .build();
        }else {
        	job = Job.builder()
                    .title(jobRequest.getTitle())
                    .description(jobRequest.getDescription())
                    .salaryMin(-1)
                    .salaryMax(-1)
                    .position(jobRequest.getPosition())
                    .workingFrom(jobRequest.getWorkingFrom())
                    .location(employer.getCity())
                    .technologies(jobRequest.getTechnologies())
                    .deadline(jobRequest.getDeadline())
                    .logo(employer.getLogo())
                    .build();
        }

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
                employer,
                job.getCreatedAt().toLocalDate());
    }

    private void checkJobPostLimit(Employer employer) {
        LocalDateTime now = LocalDateTime.now();
        
        // Lấy gói đăng ký active hiện tại của employer
        Optional<EmployerSubscription> activeSubOpt = employerSubscriptionRepository
                .findFirstByEmployer_EmployerIdAndStatusAndEndDateAfterOrderByEndDateDesc(
                        employer.getEmployerId(), "ACTIVE", now);
        
        if (activeSubOpt.isPresent()) {
            EmployerSubscription sub = activeSubOpt.get();
            VipPackage vipPackage = sub.getVipPackage();
            
            // 1. Kiểm tra giới hạn tổng số bài đăng của gói (postLimit)
            if (vipPackage.getPostLimit() != null && vipPackage.getPostLimit() > 0) {
                // Đếm tổng số bài đăng trong thời gian hiệu lực của gói
                Specification<Job> totalSpec = Specification.where(JobSpec.hasEmployerId(employer.getEmployerId()))
                        .and(JobSpec.createdBetween(sub.getStartDate(), sub.getEndDate()));
                
                long totalPosts = jobRepository.count(totalSpec);
                
                if (totalPosts >= vipPackage.getPostLimit()) {
                    throw new AppException(Code.JOB_POST_TOTAL_LIMIT_EXCEEDED);
                }
            }

            // 2. Kiểm tra giới hạn bài đăng hàng tuần (weeklyPostLimit)
            if (vipPackage.getWeeklyPostLimit() != null && vipPackage.getWeeklyPostLimit() > 0) {
                // Tính thời gian đầu tuần và cuối tuần hiện tại
                LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59);
                
                // Sử dụng Specification để đếm số lượng bài đăng trong tuần
                Specification<Job> weeklySpec = Specification.where(JobSpec.hasEmployerId(employer.getEmployerId()))
                        .and(JobSpec.createdBetween(startOfWeek, endOfWeek));
                
                long currentWeekPosts = jobRepository.count(weeklySpec);
                
                if (currentWeekPosts >= vipPackage.getWeeklyPostLimit()) {
                    throw new AppException(Code.JOB_POST_WEEKLY_LIMIT_EXCEEDED);
                }
            }
        } else {
             throw new AppException(Code.VIP_PACKAGE_NOT_FOUND);
        }
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
        ));
    }

    @Override
    @Transactional
    public Job findById(String jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new AppException(Code.JOB_NOT_FOUND));
    }

    @Override
    @Transactional
    public Optional<Job> findByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public void deleteAllById(List<String> jobId) {
    	List<Job> jobs = (List<Job>) jobRepository.findAllById(jobId);

        for (Job job : jobs) {

            for (Candidate c : job.getCandicateLiked()) {
                c.getLikedJobs().remove(job);
            }

            job.getCandicateLiked().clear();
        }

        candidateRepository.saveAll(
                jobs.stream()
                    .flatMap(j -> j.getCandicateLiked().stream())
                    .toList()
        );
        jobRepository.deleteAll(jobs);
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
                job.getEmployer(),
                job.getCreatedAt().toLocalDate());
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
        ));
    }

    @Override
    @Transactional
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public boolean reviewJob(JobReviewRequest request) {
        try {
            List<Job> jobs = (List<Job>) jobRepository.findAllById(request.getJobId());

            
            if (jobs.size() != request.getJobId().size()) {
                throw new AppException(Code.JOB_NOT_FOUND);
            }
            
            jobs.forEach(job -> job.setStatus(request.getJobStatus()));
            jobRepository.saveAll(jobs);
            
            Map<String,String> companyEmails = new HashMap<String,String>();
            
            jobs.forEach(j -> {
            	String content = String.format(j.getStatus().getMessage(), j.getTitle());
            	companyEmails.put(j.getEmployer().getEmail(),content);
            });
     
            
            companyEmails.forEach((t ,u)-> {
            	Notification notification = Notification.builder()
	            		.content(u)
	            		.isRead(false)
	            		.userId(t)
	            		.role(Role.ROLE_EMPLOYER)
	            		.type("Duyet")
	            		.from("admin")
	            		.build();
            	notificationRepository.save(notification);
	            sseUtils.sendToUser(t, u,notification.getNotiId()); 
            	
            });     
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
                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate());
    }

	@Override
	@Transactional
	public Page<Job> search(JobSearchRequest request, Pageable pageable) {
		Specification<Job> spec = Specification.where(JobSpec.keyword(request.getKeyword()))
	            .and(JobSpec.hasLocations(request.getLocation()))
	            .and(JobSpec.salaryRange(request.getSalaryRange()))
	            .and(JobSpec.workingFrom(request.getWorkingFrom()))
	            .and(JobSpec.position(request.getPosition()))
	            .and(JobSpec.language(request.getLanguage()));

	    return jobRepository.findAll(spec, pageable);
	}

	@Override
	public Page<JobResponse> findAllByStatusPending(Pageable pageable) {
		Page<Job> jobs = jobRepository.findAllByStatus(JobStatus.PENDING, pageable);
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

                job.getEmployer(),
                job.getCreatedAt().toLocalDate()
        ));
	}

}
