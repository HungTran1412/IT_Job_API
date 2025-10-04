package backend.main.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerRequest {
    String email;
    String password;
    String companyName;
    String address;
    String phone;
    String avatar;
}
