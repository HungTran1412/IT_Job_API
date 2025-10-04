package backend.main.services;

import backend.main.dto.request.EmployerRegisterRequest;
import backend.main.entities.Employer;

public interface EmployerService {
    Employer register(EmployerRegisterRequest employerRegisterRequest);
}
