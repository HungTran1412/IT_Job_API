package backend.main.services;

import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.admin.AdminRegisterRequest;
import backend.main.dto.response.UserSummaryResponse;
import backend.main.entities.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    String login(LoginRequest request);
    boolean changePassword(String email, ChangePasswordRequest request);
    void updateUserLockStatus(String userId, boolean isLocked);
    Page<UserSummaryResponse> getAllUsers(Pageable pageable, String role);
    void deleteUser(String userId);
}
