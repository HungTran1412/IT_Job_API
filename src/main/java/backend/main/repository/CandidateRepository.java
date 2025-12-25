package backend.main.repository;

import backend.main.entities.Candidate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends CrudRepository<Candidate, String> {
    Optional<Candidate> findByEmail(String email);
    
    @Query("SELECT c FROM Candidate c WHERE " +
            "c.isPrivate = false AND " +
            "(:fullname IS NULL OR :fullname = '' OR LOWER(c.fullname) LIKE LOWER(CONCAT('%', :fullname, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:softSkill IS NULL OR :softSkill = '' OR LOWER(c.softSkill) LIKE LOWER(CONCAT('%', :softSkill, '%'))) AND " +
            "(:experience IS NULL OR :experience = '' OR LOWER(c.experience) LIKE LOWER(CONCAT('%', :experience, '%'))) AND " +
            "(:technologies IS NULL OR :technologies = '' OR LOWER(c.technologies) LIKE LOWER(CONCAT('%', :technologies, '%'))) AND " +
            "(:desiredSalary IS NULL OR :desiredSalary = '' OR LOWER(c.desiredSalary) LIKE LOWER(CONCAT('%', :desiredSalary, '%')))")
    List<Candidate> searchCandidates(@Param("fullname") String fullname,
                                     @Param("email") String email,
                                     @Param("softSkill") String softSkill,
                                     @Param("experience") String experience,
                                     @Param("technologies") String technologies,
                                     @Param("desiredSalary") String desiredSalary);
}
