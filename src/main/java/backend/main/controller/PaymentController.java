package backend.main.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.configuration.AppProperties;
import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnPayService;
    private final AppProperties appProperties;

    @GetMapping("/create-payment")
    public ResponseEntity<ApiResponse<String>> createPayment(HttpServletRequest request, @RequestParam Integer orderId) {
        String paymentUrl = vnPayService.createPaymentUrl(request, orderId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(Code.CREATE_PAYMENT_URL_SUCCESSFUL.getCode())
                .message(Code.CREATE_PAYMENT_URL_SUCCESSFUL.getMessage())
                .result(paymentUrl)
                .build());
    }

    // API này vẫn giữ lại nếu cần debug hoặc FE muốn gọi chủ động
    @GetMapping("/vnpay-callback")
    public ResponseEntity<ApiResponse<Map<String, Object>>> vnpayCallback(HttpServletRequest request) {
        Map<String, Object> result = vnPayService.processPaymentCallback(request);
        String status = (String) result.get("status");
        
        if ("SUCCESS".equals(status)) {
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(Code.PAYMENT_SUCCESS.getCode())
                    .message(Code.PAYMENT_SUCCESS.getMessage())
                    .result(result)
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .code(Code.PAYMENT_FAILED.getCode())
                    .message(Code.PAYMENT_FAILED.getMessage() + " - " + (String) result.get("message"))
                    .result(result)
                    .build());
        }
    }

    // API này được cấu hình là Return URL của VNPay
    @GetMapping("/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. Xử lý logic cập nhật đơn hàng
        Map<String, Object> result = vnPayService.processPaymentCallback(request);
        
        String status = (String) result.get("status");
        String message = (String) result.get("message");
        Object orderId = result.get("orderId");

        // 2. Lấy URL Frontend từ cấu hình
        String feUrl = appProperties.getFrontend().getFeUrl();
        
        // 3. Tạo URL redirect
        StringBuilder redirectUrl = new StringBuilder(feUrl);
        if (!feUrl.endsWith("/")) {
            redirectUrl.append("/");
        }
        
        // Nếu lấy được orderId thì chuyển về trang ordersuccess/{id}
        if (orderId != null) {
            redirectUrl.append("ordersuccess/").append(orderId);
        } else {
            // Fallback nếu lỗi không xác định được đơn hàng (ví dụ checksum sai)
            redirectUrl.append("payment-return"); 
        }
        
        // Thêm tham số status và message để FE hiển thị (nếu cần)
        redirectUrl.append("?status=").append(status);
        redirectUrl.append("&message=").append(URLEncoder.encode(message, StandardCharsets.UTF_8));

        // 4. Chuyển hướng người dùng về Frontend
        response.sendRedirect(redirectUrl.toString());
    }
}
