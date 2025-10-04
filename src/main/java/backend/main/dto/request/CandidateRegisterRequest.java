package backend.main.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CandidateRegisterRequest {
    String fullname;
    String email;
    String password;
    String gender;
    String address;
    LocalDate dateOfBirth;
    LocalDate createAt;
    LocalTime udpateAt;
    String phone;
    String avatar;
    String cv;
    String role;
}
