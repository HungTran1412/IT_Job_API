package backend.main.dto.request.job;

<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
<<<<<<< HEAD
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
=======
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25

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
<<<<<<< HEAD
    String location;
=======
    List<String> location;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    @DateTimeFormat(pattern = "yyyy-M-d")
    LocalDate deadline;
}
