package backend.main.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseModel {
    @Column(name = "create_at",updatable = false, nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdAt;

    @Column(name = "update_at")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime updateAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}
