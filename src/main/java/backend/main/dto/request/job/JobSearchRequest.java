package backend.main.dto.request.job;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobSearchRequest {

	String keyword;
	List<String> location;
	String salaryRange;
	String workingFrom;
	String position;
	String language;
	int page;
	int size;
}