package backend.main.controller;

import java.util.List;

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
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequest request) {
        Order result = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code("200")
                .message("Create Order successfully")
                .result(result)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Integer id) {
        Order result = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code("200")
                .message("Get Order successfully")
                .result(result)
                .build());
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByEmployerId(@PathVariable String employerId) {
        List<Order> result = orderService.getOrdersByEmployerId(employerId);
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .code("200")
                .message("Get Orders by Employer successfully")
                .result(result)
                .build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> result = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .code("200")
                .message("Get all Orders successfully")
                .result(result)
                .build());
    }
    
    // API này dùng để test update status thủ công (sau này sẽ do VNPay callback gọi)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        Order result = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .code("200")
                .message("Update Order status successfully")
                .result(result)
                .build());
    }
}
