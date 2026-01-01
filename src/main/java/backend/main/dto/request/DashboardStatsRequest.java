package backend.main.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class DashboardStatsRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate;
}
