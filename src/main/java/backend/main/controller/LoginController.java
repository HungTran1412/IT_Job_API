package backend.main.controller;

import backend.main.dto.request.LoginRequest;
import backend.main.dto.response.LoginResponse;
import backend.main.services.LoginService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return  loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
