package backend.main.dto.request.job;

import java.util.List;

import backend.main.enums.JobStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobReviewRequest {
    List<String> jobId;
    JobStatus jobStatus;
}
