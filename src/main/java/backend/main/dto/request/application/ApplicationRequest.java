package backend.main.dto.request.application;

import java.time.LocalDateTime;

import backend.main.enums.ApplicationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationRequest {
	LocalDateTime appliedDate;
	String coverLetter;
	ApplicationStatus status;
	String cv;
	String candicateId;
	String jobId;
}
