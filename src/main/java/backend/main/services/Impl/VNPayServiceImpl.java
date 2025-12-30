package backend.main.services.Impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import backend.main.enums.Code;
import backend.main.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import backend.main.configuration.VNPayConfig;
import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.entities.Order;
import backend.main.repository.OrderRepository;
import backend.main.services.EmployerSubscriptionService;
import backend.main.services.VNPayService;
import backend.main.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {

    private final VNPayConfig vnpayConfig;
    private final OrderRepository orderRepository;
    private final EmployerSubscriptionService employerSubscriptionService;

    @Value("${app.front-end.fe-url}")
    private String feUrl;

    @Override
    public String createPaymentUrl(HttpServletRequest request, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(Code.ORDER_NOT_FOUND));

        String vnp_Version = vnpayConfig.getVersion();
        String vnp_Command = vnpayConfig.getCommand();
        String vnp_OrderInfo = order.getOrderInfo() != null ? order.getOrderInfo() : "Thanh toan don hang " + orderId;
        String vnp_TxnRef = order.getVnpTxnRef();
        String vnp_IpAddr = VNPayUtil.getIpAddress(request);
        String vnp_TmnCode = vnpayConfig.getTmnCode();

        // Số tiền cần nhân với 100 (theo quy định của VNPay)
        long amount = (long) (order.getAmount() * 100);
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", vnpayConfig.getCurrencyCode());
        
        if (order.getBankCode() != null && !order.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", order.getBankCode());
        }
        
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", vnpayConfig.getLocale());
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Thời hạn thanh toán là 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Build data to hash and query string
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpayConfig.getPayUrl() + "?" + queryUrl;
        
        return paymentUrl;
    }

    @Override
    public Map<String, Object> processPaymentCallback(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, String> fields = new HashMap<>();
            for (java.util.Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = VNPayUtil.hashAllFields(fields, vnpayConfig.getSecretKey());
            if (signValue.equals(vnp_SecureHash)) {
                String vnp_TxnRef = request.getParameter("vnp_TxnRef");
                String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
                String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
                String vnp_BankCode = request.getParameter("vnp_BankCode");
                String vnp_Amount = request.getParameter("vnp_Amount");
                
                // Tìm đơn hàng
                Order order = orderRepository.findByVnpTxnRef(vnp_TxnRef)
                        .orElseThrow(() -> new AppException(Code.ORDER_NOT_FOUND));

                boolean isSuccess = "00".equals(vnp_ResponseCode);
                
                // Cập nhật thông tin giao dịch vào Order
                order.setVnpTransactionNo(vnp_TransactionNo);
                order.setBankCode(vnp_BankCode);
                
                if (isSuccess) {
                    // Nếu trạng thái hiện tại chưa phải SUCCESS thì mới xử lý
                    if (!"SUCCESS".equals(order.getStatus())) {
                        order.setStatus("SUCCESS");
                        orderRepository.save(order);
                        
                        // Tự động kích hoạt gói VIP (Tạo Subscription)
                        EmployerSubscriptionRequest subRequest = EmployerSubscriptionRequest.builder()
                                .employerId(order.getEmployer().getEmployerId())
                                .vipPackageId(order.getVipPackage().getId())
                                .build();
                        employerSubscriptionService.createSubscription(subRequest);
                        
                        result.put("message", "Payment success");
                        result.put("status", "SUCCESS");
                    } else {
                        result.put("message", "Order already confirmed");
                        result.put("status", "SUCCESS");
                    }
                } else {
                    order.setStatus("FAILED");
                    orderRepository.save(order);
                    result.put("message", "Payment failed");
                    result.put("status", "FAILED");
                }
                result.put("orderId", order.getId());
                result.put("orderCode", order.getCode());
                result.put("amount", vnp_Amount);
                result.put("feUrl", feUrl);
            } else {
                result.put("message", "Invalid Checksum");
                result.put("status", "ERROR");
            }
        } catch (Exception e) {
            result.put("message", e.getMessage());
            result.put("status", "ERROR");
        }
        return result;
    }
}
