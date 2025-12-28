package backend.main.services;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public interface VNPayService {
    String createPaymentUrl(HttpServletRequest request, Integer orderId);
    Map<String, Object> processPaymentCallback(HttpServletRequest request);
}
