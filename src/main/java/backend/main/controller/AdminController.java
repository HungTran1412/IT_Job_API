package backend.main.controller;

import backend.main.configuration.JwtUtils;
import backend.main.dto.request.ChangePasswordRequest;
import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.admin.AdminRegisterRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Admin;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.services.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        String token = adminService.login(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
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
    public ResponseEntity<ApiResponse> changePassword(@CookieValue(value = "jwt", required = false) String token,
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
}
