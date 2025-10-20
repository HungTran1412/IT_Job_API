package backend.main.services;

import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateLoginRequest;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRegisterRequest candidateRequest);
    String login(CandidateLoginRequest candidateLoginRequest);
    Candidate updateInfo(String id, CandidateRequest request);
    Candidate verifyCandidate(String token);
    Candidate changePassword(String oldPassword, String newPassword);
}
