package backend.main.entities;

import backend.main.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_employer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employer {
    @Id
    @Column(name = "id")
    String employerId;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "company_name")
    String companyName;

    @Column(name = "address")
    String address;

    @Column(name = "create_at")
    LocalDate createAt;

    @Column(name = "update_at")
    LocalDate updateAt;

    @Column(name = "phone")
    String phone;

    @Column(name = "avatar")
    String avatar;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "enabled")
    Boolean enabled = false;

    @Column(name = "verification_token")
    String verificationToken;
}
