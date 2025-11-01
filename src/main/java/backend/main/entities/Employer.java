package backend.main.entities;

import backend.main.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    String city;

    @Column(name = "address")
    String address;

    @Column(name = "company_model")
    String companyModel;

    @Column(name = "company_employees")
    String companyEmployees;

    @Column(name = "working_time")
    LocalDateTime workingTime;

    @Column(name = "working_overtime")
    LocalDateTime workingOvertime;

    @Column(name = "description")
    String description;

    @Column(name = "phone")
    String phone;

    @Column(name = "logo")
    String logo;

    @Column(name = "enabled")
    Boolean enabled = false;
}
