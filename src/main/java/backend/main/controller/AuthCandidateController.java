package backend.main.controller;

import backend.main.dto.request.CandidateRequest;
import backend.main.dto.request.CandidateLoginRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.CandidateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
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
        candidateService.register(candidateRequest);
        apiResponse.setMessage("Register successed. Please check your email to verifiy!");
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
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody CandidateLoginRequest request,
            HttpServletResponse response){
        //Gọi service để xác thực và lấy token
        String token = candidateService.login(request);

        //Tạo cookie để lưu token
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        //Tạo đối tượng phản hồi
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(Code.LOGIN_SUCCEEDED.getCode())
                .message(Code.LOGIN_SUCCEEDED.getMessage())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
