package backend.main.dto.request.application;

import java.time.LocalDateTime;

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
	String name;
	String phone;
	String cv;
	String email;
	String candicateId;
	String jobId;
}
