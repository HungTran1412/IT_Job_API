package backend.main.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.main.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseModel{
    @Column(name = "email")
    String email;

    @JsonIgnore
    @Column(name = "password")
    String password;

    @JsonIgnore
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;

    @JsonIgnore
    @Column(name = "remember_me")
    Boolean rememberMe;

    @Column(name = "is_locked")
    @Builder.Default
    Boolean isLocked = false;
    
}
