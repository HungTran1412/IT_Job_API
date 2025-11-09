package backend.main.controller;

import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutContorller {
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse res) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        res.addHeader("Set-Cookie", deleteCookie.toString());

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(Code.LOGOUT_SUCCESSED.getCode())
                .message(Code.LOGOUT_SUCCESSED.getMessage())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
