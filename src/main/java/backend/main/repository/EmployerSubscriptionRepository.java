package backend.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.EmployerSubscription;

@Repository
public interface EmployerSubscriptionRepository extends JpaRepository<EmployerSubscription, Integer> {
    List<EmployerSubscription> findByEmployer_EmployerId(String employerId);
    
    // Tìm gói đăng ký active của employer
    Optional<EmployerSubscription> findFirstByEmployer_EmployerIdAndStatusAndEndDateAfterOrderByEndDateDesc(
            String employerId, String status, LocalDateTime now);
}
