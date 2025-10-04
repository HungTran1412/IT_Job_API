package backend.main.services;

import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRegisterRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.entities.Employer;

public interface EmployerService {
    Employer register(EmployerRegisterRequest employerRegisterRequest);
    EmployerLoginResponse login(EmployerLoginRequest employerLoginRequest);
}
