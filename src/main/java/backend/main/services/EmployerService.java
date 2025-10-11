package backend.main.services;

import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.dto.response.EmployerResponse;
import backend.main.entities.Employer;

public interface EmployerService {
    Employer register(EmployerRequest employerRequest);
    EmployerLoginResponse login(EmployerLoginRequest employerLoginRequest);
    Employer updateInfo(String id, EmployerRequest request);
    Employer verifyEmployer(String token);
    EmployerResponse getEmployerById(String id);
}
