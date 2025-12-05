package backend.main.controller;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request);
        return new ApiResponse<>(
                Code.RESET_PASSWORD_SUCCESS.getCode(),
                Code.RESET_PASSWORD_SUCCESS.getMessage()
        );
    }
}
