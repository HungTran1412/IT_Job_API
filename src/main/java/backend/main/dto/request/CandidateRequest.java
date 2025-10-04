package backend.main.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CandidateRequest {
    String fullname;
    String email;
    String password;
    String gender;
    String address;
    LocalDate dateOfBirth;
    String phone;
    String avatar;
    String cv;
}
