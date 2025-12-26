package backend.main.entities;

import backend.main.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tbl_notifications")
public class Notification extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notiId;
    private String content;

    @Column(name = "notification_type")
    private String type;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "sender")
    private String from;
}
