package backend.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/order-stats")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.<DashboardStatsResponse>builder()
                .code(Code.GET_DASHBOARD_STATS_SUCCESS.getCode())
                .message(Code.GET_DASHBOARD_STATS_SUCCESS.getMessage())
                .result(stats)
                .build());
    }
}
