package backend.main.controller;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.entities.Candidate;
import backend.main.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthCandidateController {
    @Autowired
    CandidateService candidateService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CandidateRequest candidateRequest) {
        Candidate candidate = candidateService.register(candidateRequest);
        return ResponseEntity.ok().body(candidate);
    }

    @PostMapping("/login")
    public ResponseEntity<CandidateLoginResponse> login(@RequestBody CandidateLoginRequest request) {
        return  ResponseEntity.ok().body(candidateService.login(request));
    }
}
