package backend.main.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import backend.main.enums.ApplicationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ApplicationJobResponse {
	String jobId;
    String title;
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
    List<String> location;
    LocalDate deadline;
    String logo;
    String applicationId;
    LocalDateTime appliedDate;
    String coverLetter;
    ApplicationStatus status;
    String name;
    String phone;
    String cv;
    String email;
}
