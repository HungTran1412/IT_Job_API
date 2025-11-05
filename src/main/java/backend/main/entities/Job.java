package backend.main.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import backend.main.enum.JobStatus;

@Entity
@Table(name = "tbl_job")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Job extends BaseModel{
    @Id
    String jobId;
    String tilte;
    String description;
    String requirements;
    String salary;
    String location;
    LocalDateTime deadline;
    JobStatus status;
}
