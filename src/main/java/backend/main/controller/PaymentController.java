package backend.main.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.response.ApiResponse;
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

    @GetMapping("/vnpay-callback")
    public ResponseEntity<ApiResponse<Map<String, Object>>> vnpayCallback(HttpServletRequest request) {
        Map<String, Object> result = vnPayService.processPaymentCallback(request);
        String status = (String) result.get("status");
        String code = "SUCCESS".equals(status) ? "200" : "400";
        
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .code(code)
                .message((String) result.get("message"))
                .result(result)
                .build());
    }
}
