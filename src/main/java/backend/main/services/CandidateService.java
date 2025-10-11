package backend.main.services;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRequest candidateRequest);
    String login(CandidateLoginRequest candidateLoginRequest);
    Candidate updateInfo(String id, CandidateRequest request);
    Candidate verifyCandidate(String token);
}
