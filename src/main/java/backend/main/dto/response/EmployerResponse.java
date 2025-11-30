package backend.main.dto.response;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerResponse {
    String companyName;
    List<String> city;
    String address;
    String companyModel;
    String companyEmployees;
    String workingTime;
    String workingOvertime;
    String description;
    String phone;
    String logo;
    Role role;
}
