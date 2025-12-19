package backend.main.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.employer.EmployerRegisterRequest;
import backend.main.dto.request.employer.EmployerUpdateRequest;
import backend.main.entities.Employer;
import backend.main.entities.Job;

public interface EmployerService {
    Employer register(EmployerRegisterRequest employerRequest);
    String login(LoginRequest employerLoginRequest);
    Employer updateInfo(String id, EmployerUpdateRequest request);
    Employer verifyEmployer(String token);
    Employer getById(String id);
    boolean changePassword(String email, String oldPassword, String newPassword);
    void resendVerification(String email);
    List<Job> getListJob(String jwt);
    Page<Employer> findAllOrderByJobs(Pageable pageable);
}
