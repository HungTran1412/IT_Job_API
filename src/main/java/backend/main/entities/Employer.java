package backend.main.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
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

    @Default
    @ElementCollection
    @CollectionTable(name = "employer_city", joinColumns = @JoinColumn(name = "employer_id"))
    @Column(name = "city")
    List<String> city = new ArrayList<String>();

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
    @JsonIgnore
    List<Job> jobs;

    @OneToMany(mappedBy = "employer", orphanRemoval = true)
    @JsonIgnore
    List<EmployerSubscription> subscriptions;
    
}