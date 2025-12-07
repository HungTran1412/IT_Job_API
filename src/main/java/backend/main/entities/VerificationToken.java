package backend.main.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_verification_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "token", nullable = false, unique = true)
    String token;

    @Column(name = "expiration_time", nullable = false)
    LocalDateTime expirationTime;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @OneToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    Candidate candidate;

    @OneToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    Employer employer;
}
