package backend.main.controller;

import backend.main.configuration.AppProperties;
import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.request.candidate.CandidateRequest;
import backend.main.dto.request.candidate.CandidateLoginRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.CandidateService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthCandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    AppProperties appProperties;
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody CandidateRegisterRequest request){
        Candidate candidate = candidateService.register(request);

        return ApiResponse.<String>builder()
                .code("success")
                .message("Register succeeded. Please check your email to verify!")
                .email(candidate.getEmail())
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        try {
            candidateService.verifyCandidate(token);

            return ResponseEntity.status(302)
                    .header("Location", appProperties.getFrontend().getVerifiedUrl())
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(302)
                    .header("Location", appProperties.getFrontend().getFailedUrl())
                    .build();
        }
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
