package backend.main.dto.request.candidate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class CandidateRegisterRequest {
    String fullName;
    String email;
    String password;
}
