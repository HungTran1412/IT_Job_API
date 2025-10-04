package backend.main.controller;

import backend.main.dto.request.CandidateRegisterRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
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
    public ResponseEntity<?> register(@RequestBody CandidateRegisterRequest candidateRegisterRequest) {
        Candidate candidate = candidateService.register(candidateRegisterRequest);
        return ResponseEntity.ok().body(candidate);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return candidateService.login(loginRequest);
    }
}
