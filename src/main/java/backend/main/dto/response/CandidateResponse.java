package backend.main.dto.response;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateResponse {
    String candidateId;
    String fullName;
    String email;
    String address;
    LocalDate dateOfBirth;
    String phone;
    String avatar;
    String cv;
    boolean isPrivate;
    Role role;
    String gender;
    String experience;
    String technologies;
    String softSkill;
    String desiredSalary;
}
