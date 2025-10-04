package backend.main.services;

import backend.main.dto.request.CandidateRegisterRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRegisterRequest candidateRegisterRequest);
    LoginResponse login(LoginRequest loginRequest);
}
