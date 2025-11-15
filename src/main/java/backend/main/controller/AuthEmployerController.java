package backend.main.controller;

import backend.main.configuration.AppProperties;
import backend.main.dto.request.EmailRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.employer.EmployerRegisterRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.services.EmployerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class AuthEmployerController {
    @Autowired
    EmployerService employerService;

    @Autowired
    AppProperties appProperties;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody EmployerRegisterRequest request){
        Employer e = employerService.register(request);

        return ApiResponse.<String>builder()
                .code(Code.REGISTER_SUCCESSED.getCode())
                .message(Code.REGISTER_SUCCESSED.getMessage())
                .email(e.getEmail())
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        try {
            Employer verifiedEmployer = employerService.verifyEmployer(token);

            String userId = verifiedEmployer.getEmployerId();

            String url = appProperties.getFrontend().getVerifiedCompanyUrl() + userId;

            return ResponseEntity.status(302)
                    .header("Location", url)
                    .build();

        } catch (Exception e) {
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
        String token = employerService.login(request);

        //Tạo cookie
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
        employerService.resendVerification(emailRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.builder()
                .code(Code.RESEND_COMPLETE.getCode())
                .message(Code.RESEND_COMPLETE.getMessage())
                .build());
    }
}
