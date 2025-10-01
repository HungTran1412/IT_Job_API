package backend.main.services.Impl;

import backend.main.dto.response.LoginResponse;
import backend.main.entities.Accounts;
import backend.main.repository.AccountsRepository;
import backend.main.services.LoginService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    AccountsRepository accountsRepository;

    //login method
    public LoginResponse login(String username, String password) {
        Accounts accounts = accountsRepository.findByUsername(username);

        if(accounts != null && accounts.getPassword().equals(password)) {
            return new LoginResponse(true, "Login Successful!");
        }
        return new LoginResponse(false, "Invalid username or password!");
    }
}
