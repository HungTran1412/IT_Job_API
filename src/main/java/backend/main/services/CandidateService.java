package backend.main.services;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRequest candidateRequest);
    CandidateLoginResponse login(CandidateLoginRequest candidateLoginRequest);
    Candidate updateInfo(String id, CandidateRequest request);
}
