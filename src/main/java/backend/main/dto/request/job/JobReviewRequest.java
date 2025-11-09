package backend.main.dto.request.job;

import backend.main.enums.JobStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobReviewRequest {
    String jobId;
    JobStatus jobStatus;
}
