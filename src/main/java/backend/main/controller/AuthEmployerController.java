package backend.main.controller;

import backend.main.dto.request.EmployerLoginRequest;
import backend.main.dto.request.EmployerRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Employer;
import backend.main.enums.Code;
import backend.main.services.EmployerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/company")
public class AuthEmployerController {
    @Autowired
    EmployerService employerService;

    @PostMapping("/register")
    public ApiResponse<Employer> register(@RequestBody EmployerRequest request){
        ApiResponse<Employer> apiResponse = new ApiResponse<>();
        employerService.register(request);
        apiResponse.setMessage("Register successed. Please check your email to verifiy!");
        return apiResponse;
    }

    @GetMapping("/verify")
    public Map<String, String> verify(@RequestParam("token") String token){
        Map<String, String> map = new HashMap<>();
        Employer employer = employerService.verifyEmployer(token);
        map.put("message", "Your account has been verified!");
        return map;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody EmployerLoginRequest request,
            HttpServletResponse response){
        //Gọi service để xác thực và lấy token
        String token = employerService.login(request);

        //Tạo cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        //Tạo đối tượng phản hồi
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(Code.LOGIN_SUCCEEDED.getCode())
                .message(Code.LOGIN_SUCCEEDED.getMessage())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
