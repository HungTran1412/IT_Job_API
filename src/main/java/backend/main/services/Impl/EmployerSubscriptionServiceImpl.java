package backend.main.services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import backend.main.enums.Code;
import backend.main.exception.AppException;
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
                .orElseThrow(() -> new AppException(Code.EMPLOYER_NOT_FOUND));
        
        VipPackage vipPackage = vipPackageRepository.findById(request.getVipPackageId())
                .orElseThrow(() -> new AppException(Code.VIP_PACKAGE_NOT_FOUND));

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
}
