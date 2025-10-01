package backend.main.services;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
import backend.main.entities.Candidate;

public interface CandidateServices {
    Candidate register(CandidateRequest candidateRequest);
    LoginResponse login(LoginRequest loginRequest);
}
