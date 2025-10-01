package backend.main.services;

import backend.main.dto.request.CandidateRequest;
import backend.main.entities.Candidate;

public interface CandidateServices {
    Candidate register(CandidateRequest candidateRequest);
}
