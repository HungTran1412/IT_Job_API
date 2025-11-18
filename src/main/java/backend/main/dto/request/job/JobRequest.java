package backend.main.dto.request.job;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobRequest {
    String title;
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
    String location;
    @DateTimeFormat(pattern = "yyyy-M-d")
    LocalDate deadline;
}
