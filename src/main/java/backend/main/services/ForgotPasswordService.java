package backend.main.services;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;
import backend.main.dto.request.VerifyOtpRequest;

public interface ForgotPasswordService {
    void sendOtp(ForgotPasswordRequest request);

    void verifyOtp(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);
}
