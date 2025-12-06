package backend.main.dto.response;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerResponse {
    String companyName;
<<<<<<< HEAD
    String city;
=======
    List<String> city;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    String address;
    String companyModel;
    String companyEmployees;
    String workingTime;
<<<<<<< HEAD
    LocalDateTime workingOvertime;
=======
    String workingOvertime;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    String description;
    String phone;
    String logo;
    Role role;
}
