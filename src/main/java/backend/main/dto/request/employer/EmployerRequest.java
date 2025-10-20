package backend.main.dto.request.employer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerRequest {
    String email;
    String companyName;
    String city;
    String address;
    String companyModel;
    String companyEmployees;
    LocalDateTime workingTime;
    LocalDateTime workingOvertime;
    String description;
    String phone;
    MultipartFile logo;
}
