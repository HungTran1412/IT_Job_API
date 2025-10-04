package backend.main.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerLoginResponse {
    String employerId;
    String companyName;
    String email;
    String address;
    String phone;
    LocalDate createAt;
    LocalDate udpateAt;
    String avatar;
    String role;
}
