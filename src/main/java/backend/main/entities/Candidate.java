package backend.main.entities;

import backend.main.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_candidate")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
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

    @Column(name = "enabled")
    Boolean enabled = false;

    @Column(name = "is_private")
    Boolean isPrivate = true;
}
