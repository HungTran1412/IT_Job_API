package backend.main.entities;

import java.time.LocalDateTime;

import backend.main.enums.ApplicationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Application {
	@Id
	String applicationId;
	LocalDateTime appliedDate;
	String coverLetter;
	ApplicationStatus status;
	
	@ManyToOne
	@JoinColumn(name = "candicate_id")
	Candidate candidate;
	
	@ManyToOne
	@JoinColumn(name = "job_id")
	Job job;
}
