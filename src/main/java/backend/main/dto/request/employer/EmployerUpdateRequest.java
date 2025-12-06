package backend.main.dto.request.employer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerUpdateRequest {
    String companyName;
<<<<<<< HEAD
    String city;
=======
    String city; // Sửa từ List<String> thành String
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
    MultipartFile logo;
}
