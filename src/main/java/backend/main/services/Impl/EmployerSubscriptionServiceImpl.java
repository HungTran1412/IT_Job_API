package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.utils.SendEmailHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.entities.Employer;
import backend.main.entities.EmployerSubscription;
import backend.main.entities.VipPackage;
import backend.main.repository.EmployerRepository;
import backend.main.repository.EmployerSubscriptionRepository;
import backend.main.repository.VipPackageRepository;
import backend.main.services.EmployerSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployerSubscriptionServiceImpl implements EmployerSubscriptionService {

    private final EmployerSubscriptionRepository employerSubscriptionRepository;
    private final EmployerRepository employerRepository;
    private final VipPackageRepository vipPackageRepository;
    private final SendEmailHandler sendEmailHandler;

    @Override
    public EmployerSubscription createSubscription(EmployerSubscriptionRequest request) {
        Employer employer = employerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));
        
        VipPackage vipPackage = vipPackageRepository.findById(request.getVipPackageId())
                .orElseThrow(() -> new AppException(Code.VIP_PACKAGE_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(vipPackage.getDurationDays());

        // Hủy các gói đang active cũ nếu có (để đảm bảo chỉ có 1 gói active tại 1 thời điểm)
        List<EmployerSubscription> activeSubs = employerSubscriptionRepository.findByEmployer_EmployerIdAndStatus(employer.getEmployerId(), "ACTIVE");
        for (EmployerSubscription sub : activeSubs) {
            sub.setStatus("CANCELLED");
            employerSubscriptionRepository.save(sub);
        }

        EmployerSubscription subscription = EmployerSubscription.builder()
                .employer(employer)
                .vipPackage(vipPackage)
                .startDate(now)
                .endDate(endDate)
                .status("ACTIVE") 
                .build();

        return employerSubscriptionRepository.save(subscription);
    }

    @Override
    public EmployerSubscription getSubscriptionById(Integer id) {
        return employerSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.SUBSCRIPTION_NOT_FOUND));
    }

    @Override
    public List<EmployerSubscription> getSubscriptionsByEmployerId(String employerId) {
        return employerSubscriptionRepository.findByEmployer_EmployerId(employerId);
    }

    @Override
    public List<EmployerSubscription> getAllSubscriptions() {
        return employerSubscriptionRepository.findAll();
    }

    @Override
    public Optional<EmployerSubscription> getCurrentActiveSubscription(String employerId) {
        return employerSubscriptionRepository.findFirstByEmployer_EmployerIdAndStatusAndEndDateAfterOrderByEndDateDesc(
                employerId, "ACTIVE", LocalDateTime.now());
    }

    // Chạy mỗi ngày lúc 00:00 để kiểm tra các gói hết hạn
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void checkExpiredSubscriptions() {
        log.info("Starting scheduled job: Check expired subscriptions");
        LocalDateTime now = LocalDateTime.now();
        
        // Tìm tất cả các gói đang ACTIVE nhưng đã quá hạn
        List<EmployerSubscription> expiredSubscriptions = employerSubscriptionRepository.findByStatusAndEndDateBefore("ACTIVE", now);
        
        for (EmployerSubscription sub : expiredSubscriptions) {
            try {
                // 1. Cập nhật trạng thái thành EXPIRED
                sub.setStatus("EXPIRED");
                employerSubscriptionRepository.save(sub);
                
                // 2. Gửi email thông báo
                Employer employer = sub.getEmployer();
                if (employer != null && employer.getEmail() != null) {
                    sendEmailHandler.sendVipExpirationNotification(
                        employer.getEmail(), 
                        employer.getCompanyName(), 
                        sub.getVipPackage().getName()
                    );
                    log.info("Sent expiration email to employer: {}", employer.getEmail());
                }
            } catch (Exception e) {
                log.error("Error processing expired subscription id: {}", sub.getId(), e);
            }
        }
        log.info("Finished scheduled job. Processed {} expired subscriptions.", expiredSubscriptions.size());
    }
}
