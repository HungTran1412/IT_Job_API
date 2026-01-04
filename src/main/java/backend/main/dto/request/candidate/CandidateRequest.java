package backend.main.dto.request.candidate;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CandidateRequest {
    String fullname;
    String email;
    String gender;
    String address;
    @DateTimeFormat(pattern = "yyyy/M/d")
    LocalDate dateOfBirth;
    String phone;
    MultipartFile avatar;
    Boolean isPrivate;
    String experience;
    String technologies;
    String softSkill;
    String desiredSalary;
}
