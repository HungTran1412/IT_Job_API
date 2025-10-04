package backend.main.services;

import backend.main.dto.request.CandidateRegisterRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRegisterRequest candidateRegisterRequest);
    CandidateLoginResponse login(CandidateLoginRequest candidateLoginRequest);
}
