package backend.main.dto.request.admin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class AdminRegisterRequest {
    String email;
    String password;
    String name;
}
