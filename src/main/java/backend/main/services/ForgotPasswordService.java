package backend.main.services;

import backend.main.dto.request.ForgotPasswordRequest;
import backend.main.dto.request.ResetPasswordRequest;

public interface ForgotPasswordService {
    void sendOtp(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
