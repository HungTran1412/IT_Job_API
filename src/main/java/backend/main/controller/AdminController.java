package backend.main.controller;

import backend.main.dto.request.admin.UpdateUserLockRequest;
import backend.main.utils.JwtUtils;
import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "Admin: ", description = "Đăng nhập và quản lý người dùng")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtils jwtUtils;

    final int MAX_AGE_REMEMBER = 7 * 24 * 60 * 60; // 7 ngày
    final int MAX_AGE_DEFAULT  = 24 * 60 * 60;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        String token = adminService.login(request);

        long cookieMaxAge = request.isRememberMe() ? MAX_AGE_REMEMBER : MAX_AGE_DEFAULT;

        ResponseCookie cookie = ResponseCookie.from("nimda", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        //tao doi tuong phan hoi
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(Code.LOGIN_SUCCEEDED.getCode())
                .message(Code.LOGIN_SUCCEEDED.getMessage())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping(value = "/change-password")
    public ResponseEntity<ApiResponse> changePassword(@CookieValue(value = "nimda", required = false) String token,
                                                      @RequestBody ChangePasswordRequest req) {
        //Kiem tra token
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<Candidate>builder()
                            .code(Code.TOKEN_INVALID.getCode())
                            .message("Missing token or user not logged in")
                            .build());
        }

        boolean success = adminService.changePassword(jwtUtils.extractEmail(token), req);

        if (success == true) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(Code.PASSWORD_CHANGED.getCode())
                    .message(Code.PASSWORD_CHANGED.getMessage())
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                    .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                    .build());
        }
    }

    //TODO: thêm api lấy tất cả danh sách người dùng (có phân trang) hiển thị id người dùng, tên, trạng thái khóa
    //TODO: thêm api xóa người dùng cho admin

    @PutMapping("/users/lock-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateUserLockStatus(@RequestBody UpdateUserLockRequest request) {
        adminService.updateUserLockStatus(request.getUserId(), request.getIsLocked());
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(Code.UPDATE_LOCK_SUCCEEDED.getCode())
                .message(Code.UPDATE_LOCK_SUCCEEDED.getMessage())
                .build());
    }
}
