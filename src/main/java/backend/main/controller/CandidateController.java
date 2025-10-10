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
import org.springframework.http.MediaType;
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

    @PutMapping(value = "/update/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateInfo(@PathVariable String id,
                                        @ModelAttribute CandidateRequest candidateRequest,
                                        @RequestHeader("Authorization") String authHeader) {
        System.out.println("===== [UPDATE CANDIDATE INFO] =====");
        System.out.println("Candidate ID: " + id);
        System.out.println("Token: " + authHeader);

        //Get token from header
        String token = authHeader.replace("Bearer ", "");

        //Kiem tra tinh hop le
        if(!jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }

        //get email
        String email = jwtUtils.extractEmail(token);
//        String role = jwtUtils.extractRole(token);
//        System.out.println(role);

        //chỉ chính chủ được sửa
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        System.out.println("EmaiLLLLLLLL: " + candidate.getEmail());

        if(!candidate.getEmail().equals(email)){
            System.out.println(">>> Candidate not found with id: " + id);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You can't edit another user's information!");
        }

        // Log dữ liệu gửi lên
        System.out.println("Fullname: " + candidateRequest.getFullname());
        System.out.println("Gender: " + candidateRequest.getGender());
        System.out.println("Phone: " + candidateRequest.getPhone());
        System.out.println("Avatar: " + (candidateRequest.getAvatar() != null ? candidateRequest.getAvatar().getOriginalFilename() : "null"));


        //Cap nhat
        Candidate updated = candidateService.updateInfo(id, candidateRequest);
        return ResponseEntity.ok(updated);
    }
}
