package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.entities.Employer;
import backend.main.entities.EmployerSubscription;
import backend.main.entities.VipPackage;
import backend.main.repository.EmployerRepository;
import backend.main.repository.EmployerSubscriptionRepository;
import backend.main.repository.VipPackageRepository;
import backend.main.services.EmployerSubscriptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployerSubscriptionServiceImpl implements EmployerSubscriptionService {

    private final EmployerSubscriptionRepository employerSubscriptionRepository;
    private final EmployerRepository employerRepository;
    private final VipPackageRepository vipPackageRepository;

    @Override
    public EmployerSubscription createSubscription(EmployerSubscriptionRequest request) {
        Employer employer = employerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        
        VipPackage vipPackage = vipPackageRepository.findById(request.getVipPackageId())
                .orElseThrow(() -> new RuntimeException("Vip Package not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(vipPackage.getDurationDays());

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
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
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
}
