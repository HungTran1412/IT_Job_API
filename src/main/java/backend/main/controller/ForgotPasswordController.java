package backend.main.controller;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;
import backend.main.dto.request.VerifyOtpRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.ForgotPasswordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Forgot password:", description = "Các phương thức của chức năng quên mật khẩu")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordService.sendOtp(request);
        return new ApiResponse<>(
                Code.FORGOT_PASSWORD_SUCCESS.getCode(),
                Code.FORGOT_PASSWORD_SUCCESS.getMessage(),
                request.getEmail()
        );
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody VerifyOtpRequest request, HttpServletResponse response) {
        forgotPasswordService.verifyOtp(request);

        // Tạo cookie sau khi xác thực OTP thành công
        Cookie cookie = new Cookie("otp", request.getOtp()); // Sửa: getOtp() -> getToken()
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(5 * 60);
        response.addCookie(cookie);

        return new ApiResponse<>(
                Code.VERIFY_OTP_SUCCESS.getCode(),
                Code.VERIFY_OTP_SUCCESS.getMessage()
        );
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @CookieValue(name = "otp", required = false) String otp,
            @RequestBody ResetPasswordRequest request,
            HttpServletResponse response) {

        if (otp == null || otp.isEmpty()) {
            return new ApiResponse<>(
                    Code.TOKEN_NOT_FOUND_IN_COOKIE.getCode(),
                    Code.TOKEN_NOT_FOUND_IN_COOKIE.getMessage()
            );
        }

        // Gán otp từ cookie vào trường 'token' của request object
        request.setOtp(otp); // Sửa: setOtp() -> setToken()

        forgotPasswordService.resetPassword(request);

        // Xóa cookie sau khi đổi mật khẩu thành công
        Cookie cookie = new Cookie("otp", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ApiResponse<>(
                Code.RESET_PASSWORD_SUCCESS.getCode(),
                Code.RESET_PASSWORD_SUCCESS.getMessage()
        );
    }
}
