package backend.main.dto.response;

<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import backend.main.enums.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
=======
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_NULL)
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
public class JobResponse {
    String jobId;
    String title;
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
<<<<<<< HEAD
    String location;
    LocalDateTime deadline;
    JobStatus status;
    String employerId;
    String employerName;
    String logo;
=======
    List<String> location;
    LocalDate deadline;
    JobStatus status;
    String employerId;
    String employerName;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
}
