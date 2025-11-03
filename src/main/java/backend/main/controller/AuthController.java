package backend.main.controller;

import backend.main.dto.response.ApiResponse;
import backend.main.entities.Candidate;
import backend.main.enums.Code;
import backend.main.exception.AppException;
import backend.main.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/check")
public class AuthController {
    @Autowired
    private AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> check(@CookieValue(value = "jwt", required = false) String token) {

        //Kiem tra token
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<Candidate>builder()
                            .code(Code.TOKEN_NULL.getCode())
                            .message(Code.TOKEN_NULL.getMessage())
                            .build());
        }

        //lay nguoi dung
        try {
            Object user = authService.checkToken(token);

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(Code.TOKEN_VALID.getCode())
                    .message(Code.TOKEN_VALID.getMessage())
                    .result(user)
                    .build());

        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .code(Code.UNCATEGORIZED_EXCEPTION.getCode())
                            .message(Code.UNCATEGORIZED_EXCEPTION.getMessage())
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder()
                            .code(Code.SERVER_ERROR.getCode())
                            .message("Unexpected error while checking token")
                            .build());
        }
    }
}

