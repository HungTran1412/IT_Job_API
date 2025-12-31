package backend.main.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "tbl_candidate")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Candidate extends User{
    @Id
    @Column(name = "id")
    String candidateId;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "gender")
    String gender;

    @Column(name = "address")
    String address;

    @Column(name = "dateofbirth")
    LocalDate dateOfBirth;

    @Column(name = "phone")
    String phone;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "cv")
    String cv;

    @JsonIgnore
    @Column(name = "enabled")
    @Builder.Default
    Boolean enabled = false;

    @Column(name = "is_private")
    @Builder.Default
    Boolean isPrivate = true;

    @Column(name = "experience")
    String experience;

    @Column(name = "technologies")
    String technologies;

    @Column(name = "soft_skill")
    String softSkill;

    @Column(name = "desired_salary")
    String desiredSalary;

    @JsonIgnore
    @OneToMany(mappedBy = "candidate",orphanRemoval = true)
    List<Application> applications;
    
    @JsonIgnore
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "candidate_liked_jobs",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    List<Job> likedJobs = new ArrayList<Job>();
	
}
