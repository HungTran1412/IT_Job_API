package backend.main.dto.request.candidate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class LikedJobRequest {
	String jobId;
	String candicateId;
}
