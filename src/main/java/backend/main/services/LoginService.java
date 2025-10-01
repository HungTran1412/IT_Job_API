package backend.main.services;

import backend.main.dto.response.LoginResponse;

public interface LoginService {
    LoginResponse login(String username, String password);
}
