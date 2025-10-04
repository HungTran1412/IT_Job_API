package backend.main.controller;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.CandidateRequest;
import backend.main.entities.Candidate;
import backend.main.repository.CandidateRepository;
import backend.main.services.CandidateService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class CandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    JwtUtils jwtUtils;

    public CandidateController(CandidateService candidateService, CandidateRepository candidateRepository, JwtUtils jwtUtils) {
        this.candidateService = candidateService;
        this.candidateRepository = candidateRepository;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInfo(@PathVariable String id, @RequestBody CandidateRequest candidateRequest, @RequestHeader("Authorization") String authHeader) {
        //Get token from header
        String token = authHeader.replace("Bearer ", "");

        //Kiem tra tinh hop le
        if(!jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }

        //get email
        String email = jwtUtils.extractEmail(token);
        String role = jwtUtils.extractRole(token);
        System.out.println(role);

        //chỉ chính chủ được sửa
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        System.out.println("EmaiLLLLLLLL: " + candidate.getEmail());

        if(!candidate.getEmail().equals(email)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You can't edit another uesr's information!");
        }

        //Cap nhat
        Candidate updated = candidateService.updateInfo(id, candidateRequest);
        return ResponseEntity.ok(updated);
    }
}
