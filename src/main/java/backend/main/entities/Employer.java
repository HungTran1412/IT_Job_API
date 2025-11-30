package backend.main.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_employer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employer extends User {
    @Id
    @Column(name = "id")
    String employerId;

    @Column(name = "company_name")
    String companyName;

    @Column(name = "city")
    List<String> city;

    @Column(name = "address")
    String address;

    @Column(name = "company_model")
    String companyModel;

    @Column(name = "company_employees")
    String companyEmployees;

    @Column(name = "working_time")
    String workingTime;

    @Column(name = "working_overtime")
    String workingOvertime;

    @Column(name = "description")
    String description;

    @Column(name = "phone")
    String phone;

    @Column(name = "logo")
    String logo;

    @Column(name = "enabled")
    Boolean enabled = false;
    
    @OneToMany(mappedBy = "employer",orphanRemoval = true)
    List<Job> jobs;
    
}
