package backend.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.EmployerSubscription;

@Repository
public interface EmployerSubscriptionRepository extends JpaRepository<EmployerSubscription, Integer> {
    List<EmployerSubscription> findByEmployer_EmployerId(String employerId);
}
