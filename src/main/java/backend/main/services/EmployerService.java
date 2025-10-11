package backend.main.services;

import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.entities.Employer;

public interface EmployerService {
    Employer register(EmployerRequest employerRequest);
    String login(EmployerLoginRequest employerLoginRequest);
    Employer updateInfo(String id, EmployerRequest request);
    Employer verifyEmployer(String token);
}
