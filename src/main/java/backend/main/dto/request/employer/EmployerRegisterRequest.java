package backend.main.dto.request.employer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerRegisterRequest {
    String companyName;
    String email;
    String password;
}
