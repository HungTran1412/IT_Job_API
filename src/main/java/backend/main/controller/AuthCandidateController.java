package backend.main.controller;

import backend.main.configuration.AppProperties;
import backend.main.dto.request.EmailRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.candidate.CandidateRegisterRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.CandidateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Auth Candidate:", description = "Đăng nhập đăng ký và xác thực tài khoản cho ứng viên")
public class AuthCandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    AppProperties appProperties;
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody CandidateRegisterRequest request){
        Candidate candidate = candidateService.register(request);

        return ApiResponse.<String>builder()
                .code(Code.REGISTER_SUCCESSED.getCode())
                .message(Code.REGISTER_SUCCESSED.getMessage())
                .email(candidate.getEmail())
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        try {
            Candidate verifiedCandidate = candidateService.verifyCandidate(token);

            String userId = verifiedCandidate.getCandidateId();

            String redirectUrl = appProperties.getFrontend().getVerifiedUrl() + userId;

            // 4. Trả về response redirect với URL mới
            return ResponseEntity.status(302)
                    .header("Location", redirectUrl)
                    .build();

        } catch (Exception e) {
            // Giữ nguyên logic xử lý lỗi của bạn, redirect về trang failed
            return ResponseEntity.status(302)
                    .header("Location", appProperties.getFrontend().getFailedUrl())
                    .build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequest request,
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

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<?>> resend(@RequestBody EmailRequest emailRequest){
        candidateService.resendVerification(emailRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.builder()
                        .code(Code.RESEND_COMPLETE.getCode())
                        .message(Code.RESEND_COMPLETE.getMessage())
                .build());
    }
}
