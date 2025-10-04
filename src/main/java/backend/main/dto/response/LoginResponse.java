package backend.main.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String candidateId;
    String email;
    String fullname;
    String gender;
    String address;
    LocalDate dateOfBirth;
    LocalDate createAt;
    LocalDate udpateAt;
    String phone;
    String avatar;
    String role;
}
