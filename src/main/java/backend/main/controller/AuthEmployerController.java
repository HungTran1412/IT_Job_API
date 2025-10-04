package backend.main.controller;

import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRegisterRequest;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.entities.Employer;
import backend.main.services.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class AuthEmployerController {
    @Autowired
    EmployerService employerService;

    @PostMapping("/register")
    ResponseEntity<?> registerEmployer(@RequestBody EmployerRegisterRequest employerRegisterRequest){
        Employer employer = employerService.register(employerRegisterRequest);
        return ResponseEntity.ok().body(employer);
    }

    @PostMapping("/login")
    EmployerLoginResponse loginEmployer(@RequestBody EmployerLoginRequest employerLoginRequest){
        return employerService.login(employerLoginRequest);
    }
}
