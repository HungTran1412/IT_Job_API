package backend.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_order")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    Employer employer;

    @ManyToOne
    @JoinColumn(name = "vip_package_id", nullable = false)
    VipPackage vipPackage;

    @Column(name = "amount", nullable = false)
    Double amount;

    @Column(name = "status")
    String status; // PENDING, SUCCESS, FAILED, CANCELLED

    @Column(name = "vnp_txn_ref")
    String vnpTxnRef; // Mã đơn hàng gửi sang VNPay (có thể dùng chính ID hoặc sinh mã riêng)

    @Column(name = "vnp_transaction_no")
    String vnpTransactionNo; // Mã giao dịch ghi nhận tại hệ thống VNPay

    @Column(name = "bank_code")
    String bankCode; // Mã ngân hàng thanh toán

    @Column(name = "order_info")
    String orderInfo; // Nội dung thanh toán
}
