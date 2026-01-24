package backend.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    List<Order> findByEmployer_EmployerId(String employerId);
    Optional<Order> findByVnpTxnRef(String vnpTxnRef);
    
    @Query("SELECT o FROM Order o WHERE o.employer.employerId = :employerId AND o.status = 'SUCCESS' ORDER BY o.createdAt DESC LIMIT 1")
    Optional<Order> findLatestSuccessfulOrderByEmployer(@Param("employerId") String employerId);
}
