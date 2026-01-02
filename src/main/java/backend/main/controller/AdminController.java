package backend.main.controller;

import backend.main.dto.request.PageRequestDto;
import backend.main.dto.request.admin.UpdateUserLockRequest;
import backend.main.dto.response.UserSummaryResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/users/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserSummaryResponse>>> getAllUsers(@RequestBody PageRequestDto requestDto) {
        Pageable pageable = requestDto.toPageable();
        Page<UserSummaryResponse> users = adminService.getAllUsers(pageable, requestDto.getRole());
        return ResponseEntity.ok(ApiResponse.<Page<UserSummaryResponse>>builder()
                .code(Code.GET_INFO_SUCCEEDED.getCode())
                .message(Code.GET_INFO_SUCCEEDED.getMessage())
                .result(users)
                .build());
    }

    @PutMapping("/users/lock-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateUserLockStatus(@RequestBody UpdateUserLockRequest request) {
        adminService.updateUserLockStatus(request.getUserId(), request.getIsLocked());
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(Code.UPDATE_INFO_SUCCEEDED.getCode())
                .message("Cập nhật trạng thái khóa người dùng thành công")
                .build());
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(Code.DELETED_USER_SUCCESSFULLY.getCode())
                .message(Code.DELETED_USER_SUCCESSFULLY.getMessage())
                .build());
    }
}
