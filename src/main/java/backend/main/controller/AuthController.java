package backend.main.controller;

import backend.main.dto.request.CandidateRequest;
import backend.main.entities.Candidate;
import backend.main.services.CandidateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    CandidateServices candidateServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CandidateRequest candidateRequest) {
        Candidate candidate = candidateServices.register(candidateRequest);
        return ResponseEntity.ok().body(candidate);
    }
}
