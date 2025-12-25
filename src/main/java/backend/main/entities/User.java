package backend.main.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.main.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
    
}
