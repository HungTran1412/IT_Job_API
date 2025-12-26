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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @Column(name = "name", nullable = false)
    String name; // Tên gói (VD: VIP Silver, VIP Gold)

    @Column(name = "price", nullable = false)
    Double price; // Giá gói

    @Column(name = "duration_days")
    Integer durationDays; // Thời hạn của gói (VD: 30 ngày)

    @Column(name = "post_limit")
    Integer postLimit; // Số lượng bài đăng cho phép trong gói này

    @Column(name = "job_post_duration_days")
    Integer jobPostDurationDays; // Bài đăng thuộc gói này sẽ tồn tại bao lâu (VD: 15 ngày, 30 ngày)

    @Column(name = "description", columnDefinition = "TEXT")
    String description; // Mô tả quyền lợi

    @Column(name = "is_active")
    @lombok.Builder.Default
    Boolean isActive = true; // Trạng thái gói (đang bán hay ngừng bán)
}