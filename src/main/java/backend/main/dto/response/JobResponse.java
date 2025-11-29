package backend.main.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import backend.main.enums.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_NULL)
public class JobResponse {
    String jobId;
    String title;
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
    String location;
    LocalDate deadline;
    JobStatus status;
    String employerId;
    String employerName;
    String logo;
}
