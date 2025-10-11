package backend.main.controller;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.CandidateLoginResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class AuthCandidateController {
    @Autowired
    CandidateService candidateService;

    @PostMapping("/register")
    public ApiResponse<Candidate> register(@RequestBody CandidateRequest candidateRequest){
        ApiResponse<Candidate> apiResponse = new ApiResponse<>();

        apiResponse.setResult(candidateService.register(candidateRequest));
        return apiResponse;
    }

    @GetMapping("/verify")
    public Map<String, String> verify(@RequestParam("token") String token){
        Map<String, String> map = new HashMap<>();
        Candidate candidate = candidateService.verifyCandidate(token);
        map.put("message", "Your account has been verified!");
        return map;
    }

    @PostMapping("/login")
    public ApiResponse<CandidateLoginResponse> login(@RequestBody CandidateLoginRequest candidateLoginRequest){
        CandidateLoginResponse response = candidateService.login(candidateLoginRequest);

        return ApiResponse.<CandidateLoginResponse>builder()
                .code(Code.LOGIN_SUCCEEDED.getCode())
                .message(Code.LOGIN_SUCCEEDED.getMessage())
                .build();
    }
}
