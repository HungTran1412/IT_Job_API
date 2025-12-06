package backend.main.dto.request.employer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerUpdateRequest {
    String companyName;
    String city;
    String address;
    String companyModel;
    String companyEmployees;
    String workingTime;
    String workingOvertime;
    String description;
    String phone;
    MultipartFile logo;
}
