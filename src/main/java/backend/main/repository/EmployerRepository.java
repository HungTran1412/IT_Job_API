package backend.main.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import backend.main.entities.Employer;

public interface EmployerRepository extends JpaRepository<Employer, String> {
    Optional<Employer> findByEmail(String email);
    Optional<Employer> findByCompanyName(String name);
    
    @Query("""
            SELECT e 
            FROM Employer e 
            LEFT JOIN e.jobs j
            GROUP BY e
            ORDER BY COUNT(j) DESC
        """)
        Page<Employer> findAllOrderByJobs(Pageable pageable);
}
