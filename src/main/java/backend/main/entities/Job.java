package backend.main.entities;

import java.time.LocalDateTime;
import java.util.List;

import backend.main.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_job")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Job extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_id")
    String jobId;
    
    String title;
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
    String location;
    LocalDateTime deadline;

    @Builder.Default
    JobStatus status = JobStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @JsonIgnore
    Employer employer;

    @OneToMany(mappedBy = "job")
    List<Application> applications ;
}
