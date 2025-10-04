package backend.main.dto.request;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerRegisterRequest {
    String email;
    String password;
    String companyName;
    String address;
    String phone;
    String avatar;
}
