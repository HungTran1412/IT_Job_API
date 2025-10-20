package backend.main.services;

import backend.main.dto.request.employer.EmployerLoginRequest;
import backend.main.dto.request.employer.EmployerRegisterRequest;
import backend.main.dto.request.employer.EmployerRequest;
import backend.main.entities.Employer;

public interface EmployerService {
    Employer register(EmployerRegisterRequest employerRequest);
    String login(EmployerLoginRequest employerLoginRequest);
    Employer updateInfo(String id, EmployerRequest request);
    Employer verifyEmployer(String token);
}
