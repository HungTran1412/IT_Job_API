package backend.main.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.main.enums.JobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "job_id")
    String jobId;

    String title;
    @Column(columnDefinition = "LONGTEXT")
    String description;
    int salaryMin;
    int salaryMax;
    String position;
    List<String> technologies;
    String workingFrom;
    List<String> location;
    LocalDate deadline;
    String logo;

    @Builder.Default
    JobStatus status = JobStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @JsonIgnore
    Employer employer;

    @ManyToOne
    @JoinColumn(name = "candicate_id")
    @JsonIgnore
    Candidate candicateLiked;

    @OneToMany(mappedBy = "job")
    List<Application> applications ;
}