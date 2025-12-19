package backend.main.dto.request.job;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequest {

    private String keyword;
    private List<String> location;
    private String salaryRange;
    private String workingFrom;
    private String position;
    private String language;
}