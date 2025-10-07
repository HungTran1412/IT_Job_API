package backend.main.controller;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.EmployerRequest;
import backend.main.entities.Employer;
import backend.main.repository.EmployerRepository;
import backend.main.services.EmployerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;

@RestController
@RequestMapping("/company")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerController {
    @Autowired
    EmployerService employerService;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    JwtUtils jwtUtils;

    public EmployerController(EmployerService employerService, EmployerRepository employerRepository, JwtUtils jwtUtils) {
        this.employerService = employerService;
        this.employerRepository = employerRepository;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInfo(@PathVariable String id, @RequestBody EmployerRequest request, @RequestHeader(value = "Authorization") String authHeader) {
        //lay token
        String token = authHeader.replace("Bearer ", "");

        //Check valid
        if(!jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }

        //Lay email va role
        String email = jwtUtils.extractEmail(token);
        String role = jwtUtils.extractRole(token);
        System.out.println(role);

        //Chinh chinh nguoi dung moi duoc sua
        Employer employer = employerRepository.findById(id).orElse(null);
        System.out.println("EmaiLLLLLLLL: " + employer.getEmail());

        if(!employer.getEmail().equals(email)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You can't edit another user's information!");
        }

        //Update
        Employer updated = employerService.updateInfo(id, request);
        return ResponseEntity.ok(updated);
    }
}
