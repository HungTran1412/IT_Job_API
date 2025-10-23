package backend.main.services;

import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateLoginRequest;
import backend.main.entities.Candidate;

public interface CandidateService {
    Candidate register(CandidateRegisterRequest candidateRequest);
    void resendVerification(String email);
    String login(CandidateLoginRequest candidateLoginRequest);
    Candidate updateInfo(String id, CandidateRequest request);
    Candidate verifyCandidate(String token);
    boolean changePassword(String email, String oldPassword, String newPassword);

}
