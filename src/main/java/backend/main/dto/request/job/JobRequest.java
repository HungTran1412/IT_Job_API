package backend.main.dto.request.job;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobRequest {
    String title;
    String description;
    String requirements;
    String salary;
    String location;
    LocalDateTime deadline;
}
