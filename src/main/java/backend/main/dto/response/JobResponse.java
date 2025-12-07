package backend.main.dto.response;

import java.time.LocalDate;
import java.util.List;

import backend.main.entities.Employer;
import backend.main.enums.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
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
    JobStatus status;
    String logo;
    Employer employer;
}
