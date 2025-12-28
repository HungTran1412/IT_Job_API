package backend.main.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.response.ApiResponse;
import backend.main.enums.Code;
import backend.main.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnPayService;

    @GetMapping("/create-payment")
    public ResponseEntity<ApiResponse<String>> createPayment(HttpServletRequest request, @RequestParam Integer orderId) {
        String paymentUrl = vnPayService.createPaymentUrl(request, orderId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code("200")
                .message("Create payment URL successfully")
                .result(paymentUrl)
                .build());
    }

    // API này trả về JSON để xử lý logic (được gọi bởi trang HTML bên dưới)
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
                    .message((String) result.get("message"))
                    .result(result)
                    .build());
        }
    }

    // API này trả về giao diện HTML (được cấu hình làm Return URL của VNPay)
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn() {
        try {
            Resource resource = new ClassPathResource("payment-return.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("<h1>Error loading payment page</h1>");
        }
    }
}
