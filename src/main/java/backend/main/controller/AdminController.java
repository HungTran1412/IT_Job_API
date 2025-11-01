package backend.main.controller;

import backend.main.dto.request.LoginRequest;
import backend.main.dto.request.admin.AdminRegisterRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Admin;
import backend.main.enums.Code;
import backend.main.services.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public ApiResponse<String> registerAdmin(@RequestBody AdminRegisterRequest request) {
        Admin a = adminService.addAdmin(request);

        return ApiResponse.<String>builder()
                .code(Code.REGISTER_SUCCESSED.getCode())
                .message("Đăng ký thành công!")
                .email(request.getEmail())
                .build();
    }

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
}
