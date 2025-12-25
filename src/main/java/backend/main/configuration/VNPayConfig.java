package backend.main.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {
    String payUrl;        // vnpay.pay-url
    String returnUrl;     // vnpay.return-url (URL callback của ứng dụng sau khi thanh toán)
    String tmnCode;       // vnpay.tmn-code (Mã website của bạn tại VNPAY)
    String secretKey;     // vnpay.secret-key (Key bảo mật do VNPAY cung cấp)
    String apiUrl;        // vnpay.api-url (URL API truy vấn của VNPAY)
    String version = "2.1.0"; // vnpay.version (Phiên bản API, thường không đổi)
    String command = "pay";   // vnpay.command (Mã API request, thường là "pay")
    String currencyCode = "VND"; // vnpay.currency-code (Mã tiền tệ, mặc định "VND")
    String locale = "vn";
}
