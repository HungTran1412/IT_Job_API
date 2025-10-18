package backend.main.dto.request;

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
    String password;
    String gender;
    String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;
    String phone;
    MultipartFile avatar;
    MultipartFile cv;
}
