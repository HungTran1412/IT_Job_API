package backend.main.controller;

import java.util.List;

import backend.main.enums.Code;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.OrderRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.Order;
import backend.main.services.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    @Autowired
    OrderService orderService;

    //TODO: viết api lấy ra tổng số đơn hàng,tổng số công việc cho trang dashboard
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequest request) {
        Order result = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code(Code.CREATE_ORDER_SUCCESS.getCode())
                .message(Code.CREATE_ORDER_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Integer id) {
        Order result = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code(Code.GET_ORDER_SUCCESS.getCode())
                .message(Code.GET_ORDER_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByEmployerId(@PathVariable String employerId) {
        List<Order> result = orderService.getOrdersByEmployerId(employerId);
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .code(Code.GET_ORDER_SUCCESS.getCode())
                .message(Code.GET_ORDER_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    //TODO:-viết api riêng lấy tất cả đơn hàng có lọc theo từng tháng, hoặc cho phép truyền vào từ tháng 1 đến tháng 9 . Ví dụ(startDate=1&endDate=9)

    //TODO: -lấy ra danh sách đơn hàng có cả phân trang
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> result = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .code(Code.GET_ORDER_SUCCESS.getCode())
                .message(Code.GET_ORDER_SUCCESS.getMessage())
                .result(result)
                .build());
    }
    
    // API này dùng để test update status thủ công (sau này sẽ do VNPay callback gọi)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        Order result = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code(Code.UPDATE_ORDER_STATUS_SUCCESS.getCode())
                .message(Code.UPDATE_ORDER_STATUS_SUCCESS.getMessage())
                .result(result)
                .build());
    }
}
