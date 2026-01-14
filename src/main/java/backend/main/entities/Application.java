package backend.main.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.main.enums.ApplicationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Application extends BaseModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String applicationId;
	
	LocalDateTime appliedDate;
	String coverLetter;
	
	@Builder.Default
    @JsonIgnore
	ApplicationStatus status = ApplicationStatus.PENDING;
	
	String name;
	String phone;
	String cv;
	String email;
	
	@ManyToOne
	@JoinColumn(name = "candicate_id")
    @JsonIgnore
	Candidate candidate;
	
	@ManyToOne
	@JoinColumn(name = "job_id")
    @JsonIgnore
	Job job;
}
