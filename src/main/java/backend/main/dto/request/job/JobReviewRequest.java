package backend.main.dto.request.job;

<<<<<<< HEAD
import backend.main.enums.JobStatus;
import lombok.*;
=======
import java.util.List;

import backend.main.enums.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobReviewRequest {
<<<<<<< HEAD
    String jobId;
=======
    List<String> jobId;
>>>>>>> 47fc9314ac8633e043cb4c92d637b83ca6f1cb25
    JobStatus jobStatus;
}
