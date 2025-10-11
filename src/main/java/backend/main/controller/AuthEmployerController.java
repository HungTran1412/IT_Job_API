package backend.main.controller;

import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.EmployerLoginResponse;
import backend.main.entities.Candidate;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.services.CandidateService;
import backend.main.services.EmployerService;
import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/company")
public class AuthEmployerController {
    @Autowired
    EmployerService employerService;

    @PostMapping("/register")
    public ApiResponse<Employer> register(@RequestBody EmployerRequest request){
        ApiResponse<Employer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(employerService.register(request));
        return apiResponse;
    }

    @GetMapping("/verify")
    public Map<String, String> verify(@RequestParam("token") String token){
        Map<String, String> map = new HashMap<>();
        Employer employer = employerService.verifyEmployer(token);
        map.put("message", "Your account has been verified!");
        return map;
    }

    @PostMapping("/login")
    public ApiResponse<EmployerLoginResponse> login(@RequestBody EmployerLoginRequest request){
        EmployerLoginResponse response = employerService.login(request);

        return ApiResponse.<EmployerLoginResponse>builder()
                .code(Code.LOGIN_SUCCEEDED.getCode())
                .message(Code.LOGIN_SUCCEEDED.getMessage())
                .result(response)
                .build();
    }
}
