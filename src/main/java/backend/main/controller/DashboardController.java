package backend.main.controller;

import backend.main.dto.request.DashboardStatsRequest;
import backend.main.dto.response.OrderStatsResponse;
import backend.main.services.OrderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.response.ApiResponse;
import backend.main.dto.response.DashboardStatsResponse;
import backend.main.enums.Code;
import backend.main.services.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardController {

    @Autowired
    DashboardService dashboardService;
    @Autowired
    OrderService orderService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.<DashboardStatsResponse>builder()
                .code(Code.GET_DASHBOARD_STATS_SUCCESS.getCode())
                .message(Code.GET_DASHBOARD_STATS_SUCCESS.getMessage())
                .result(stats)
                .build());
    }

    @PostMapping("/order-stats")
    public ResponseEntity<ApiResponse<OrderStatsResponse>> getOrderStats(@RequestBody DashboardStatsRequest request) {
        OrderStatsResponse result = orderService.getOrderStats(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(ApiResponse.<OrderStatsResponse>builder()
                .code(Code.GET_DASHBOARD_STATS_SUCCESS.getCode())
                .message(Code.GET_DASHBOARD_STATS_SUCCESS.getMessage())
                .result(result)
                .build());
    }
}
