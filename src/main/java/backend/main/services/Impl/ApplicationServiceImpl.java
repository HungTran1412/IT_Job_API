package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import backend.main.configuration.AppProperties;
import backend.main.entities.Application;
import backend.main.entities.Candidate;
import backend.main.entities.Job;
import backend.main.entities.Notification;
import backend.main.enums.ApplicationStatus;
import backend.main.enums.Code;
import backend.main.enums.Role;
import backend.main.exception.AppException;
import backend.main.repository.ApplicationRepository;
import backend.main.repository.CandidateRepository;
import backend.main.repository.JobRepository;
import backend.main.repository.NotificationRepository;
import backend.main.services.ApplicationService;
import backend.main.utils.CloudinaryFileUpload;
import backend.main.utils.SseUtils;
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
    NotificationRepository notificationRepository;
    SseUtils sseUtils;
    AppProperties appProperties;

    @Override
    @Transactional
    public Application save(String name, String phone, String email, MultipartFile cv, String jobId) {
    	
    	try {
			Job job = jobRepository.findById(jobId).orElseThrow((() -> new AppException(Code.JOB_NOT_FOUND)));
			
			String context = SecurityContextHolder.getContext().getAuthentication().getName();

			Candidate candidate = candidateRepository.findByEmail(context).orElseThrow(()-> new AppException(Code.USER_NOT_FOUND));
			
			String cvString = cloudinaryFileUpload.uploadCv(cv);
			
			Application application = applicationRepository.findByJobAndCandidate(job, candidate);
			
			if(application!=null) {
				throw new AppException(Code.APPLY_FAIL);
			}else {
				application = Application.builder()
						.appliedDate(LocalDateTime.now())
						.name(name)
						.email(email)
						.phone(phone)
						.cv(cvString)
						.candidate(candidate)
						.job(job)
						.build();
			}
			
			Application savedApplication = applicationRepository.save(application);

			// Notify candidate
			String candidateContent = "Bạn đã ứng tuyển thành công công việc " + job.getTitle();
			Notification candidateNotification = Notification.builder()
					.content(candidateContent)
					.isRead(false)
					.userId(candidate.getEmail())
					.role(Role.ROLE_CANDIDATE)
					.type("Apply")
					.from(job.getEmployer().getCompanyName())
					.build();
			notificationRepository.save(candidateNotification);
			sseUtils.sendToUser(candidate.getEmail(), candidateContent, candidateNotification.getNotiId());

			// Notify employer
			String employerContent = "Có một ứng viên mới cho công việc " + job.getTitle();
			Notification employerNotification = Notification.builder()
					.content(employerContent)
					.isRead(false)
					.userId(job.getEmployer().getEmail())
					.role(Role.ROLE_EMPLOYER)
					.type("NewApplication")
					.from(candidate.getEmail())
					.build();
			notificationRepository.save(employerNotification);
			sseUtils.sendToUser(job.getEmployer().getEmail(), employerContent, employerNotification.getNotiId());

			return savedApplication;
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

            String content = "";
            String type = "";

            if (status == ApplicationStatus.REVIEWING) {
                content = "Nhà tuyển dụng đã xem hồ sơ của bạn cho công việc " + application.getJob().getTitle();
                type = "ApplicationViewed";
            } else if (status == ApplicationStatus.REJECTED) {
                content = "Nhà tuyển dụng đã từ chối hồ sơ của bạn cho công việc " + application.getJob().getTitle();
                type = "ApplicationRejected";
            } else if (status == ApplicationStatus.APPLIED) {
                // Applied status is set on creation, notification already sent.
                // Or maybe an employer can revert to this status? If so, a notification might be needed.
                // For now, assume no notification on re-setting to APPLIED.
                return;
            }


            if (!content.isEmpty()) {
                Notification notification = Notification.builder()
                        .content(content)
                        .isRead(false)
                        .userId(application.getCandidate().getEmail())
                        .role(Role.ROLE_CANDIDATE)
                        .type(type)
                        .from(application.getJob().getEmployer().getCompanyName())
                        .build();
                notificationRepository.save(notification);
                sseUtils.sendToUser(application.getCandidate().getEmail(), content, notification.getNotiId());
            }
        }
    }

    @Override
    @Transactional
    public void delete(String applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}