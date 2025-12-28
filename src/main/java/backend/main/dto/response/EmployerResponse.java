package backend.main.dto.response;

import java.util.List;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployerResponse {
	String employerId;
    String email;
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
    int createdJobs;
}
