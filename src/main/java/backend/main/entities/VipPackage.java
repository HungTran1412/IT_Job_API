package backend.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_vip_package")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VipPackage extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "code", unique = true, nullable = false)
    String code;

    @Column(name = "name", nullable = false)
    String name; 

    @Column(name = "price", nullable = false)
    Double price; 

    @Column(name = "duration_days")
    Integer durationDays; 

    @Column(name = "post_limit")
    Integer postLimit; 

    @Column(name = "weekly_post_limit")
    Integer weeklyPostLimit;

    @Column(name = "job_post_duration_days")
    Integer jobPostDurationDays; 

    @Column(name = "description", columnDefinition = "TEXT")
    String description; 

    @Column(name = "is_active")
    @lombok.Builder.Default
    Boolean isActive = true; 
}
