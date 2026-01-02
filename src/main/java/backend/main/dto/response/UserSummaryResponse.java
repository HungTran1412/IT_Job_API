package backend.main.dto.response;

import backend.main.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryResponse {
    String id;
    String name; // Fullname (Candidate) or CompanyName (Employer)
    String email;
    Role role;
    Boolean isLocked;
    Boolean enabled;
}
