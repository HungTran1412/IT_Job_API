package backend.main.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateSearchRequest;
import backend.main.dto.response.CandidateResponse;
import backend.main.entities.Candidate;
import backend.main.entities.Job;

public interface CandidateService {
    Candidate register(CandidateRegisterRequest candidateRequest);
    void resendVerification(String email);
    String login(LoginRequest candidateLoginRequest);
    Candidate updateInfo(String id, CandidateRequest request);
    Candidate verifyCandidate(String token);
    boolean changePassword(String email, String oldPassword, String newPassword);
    boolean addLikedJob(String jobId, String candicateId);
    List<Job> getApplied();
    List<Job> getLikedJobs();
    CandidateResponse getInfor(String id);
	boolean unLikedJob(String jobId, String candidateId);
    Page<CandidateResponse> searchCandidates(CandidateSearchRequest request, Pageable pageable);
    CandidateResponse getCandidateById(String id);
}
