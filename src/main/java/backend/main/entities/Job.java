package backend.main.entities;

import java.time.LocalDateTime;
import java.util.List;

import backend.main.enums.JobStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    String jobId;
    String tilte;
    String description;
    String requirements;
    String salary;
    String location;
    LocalDateTime deadline;
    JobStatus status;
    
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;
    
    @OneToMany(mappedBy = "job")
    List<Application> applications ;
}
