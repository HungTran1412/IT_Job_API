package backend.main.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CandidateRequest {
    String fullname;
    String email;
    String password;
    String address;
    LocalDate dateOfBirth;
    LocalDate createAt;
    LocalTime udpateAt;
    String phone;
    String avatar;
}
