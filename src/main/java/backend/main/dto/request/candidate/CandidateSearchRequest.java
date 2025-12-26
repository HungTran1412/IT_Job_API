package backend.main.dto.request.candidate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateSearchRequest {
    String fullname;
    String email;
    String softSkill;
    String experience;
    String technologies;
    String desiredSalary;
}
