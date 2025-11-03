package backend.main.services;

import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.admin.AdminRegisterRequest;
import backend.main.entities.Admin;

public interface AdminService {
    Admin addAdmin(AdminRegisterRequest request);
    String login(LoginRequest request);
    boolean changePassword(String email, ChangePasswordRequest request);
}
