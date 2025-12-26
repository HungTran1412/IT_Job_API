package backend.main.dto.request;

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
public class VipPackageRequest {
    String name;
    Double price;
    Integer durationDays;
    Integer postLimit;
    Integer jobPostDurationDays;
    String description;
    Boolean isActive;
}
