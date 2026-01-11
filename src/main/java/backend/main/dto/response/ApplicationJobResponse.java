package backend.main.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import backend.main.entities.Application;
import backend.main.entities.Job;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationJobResponse {
    private Job job;
    private List<Application> application;
}
