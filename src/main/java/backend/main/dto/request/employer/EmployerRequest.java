package backend.main.dto.request.employer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EmployerRequest {
    String email;
    String password;
    String companyName;
    String address;
    String phone;
    MultipartFile avatar;
}
