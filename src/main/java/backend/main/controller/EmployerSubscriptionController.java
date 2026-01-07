package backend.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.EmployerSubscription;
import backend.main.enums.Code;
import backend.main.services.EmployerSubscriptionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employer-subscriptions")
@RequiredArgsConstructor
public class EmployerSubscriptionController {

    private final EmployerSubscriptionService employerSubscriptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployerSubscription>> createSubscription(@RequestBody EmployerSubscriptionRequest request) {
        EmployerSubscription result = employerSubscriptionService.createSubscription(request);
        return ResponseEntity.ok(ApiResponse.<EmployerSubscription>builder()
                .code(Code.CREATE_SUBSCRIPTION_SUCCESS.getCode())
                .message(Code.CREATE_SUBSCRIPTION_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployerSubscription>> getSubscriptionById(@PathVariable Integer id) {
        EmployerSubscription result = employerSubscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(ApiResponse.<EmployerSubscription>builder()
                .code(Code.GET_SUBSCRIPTION_SUCCESS.getCode())
                .message(Code.GET_SUBSCRIPTION_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<ApiResponse<List<EmployerSubscription>>> getSubscriptionsByEmployerId(@PathVariable String employerId) {
        List<EmployerSubscription> result = employerSubscriptionService.getSubscriptionsByEmployerId(employerId);
        return ResponseEntity.ok(ApiResponse.<List<EmployerSubscription>>builder()
                .code(Code.GET_SUBSCRIPTION_SUCCESS.getCode())
                .message(Code.GET_SUBSCRIPTION_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/package/{employerId}/current")
    @PreAuthorize("hasRole('ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<EmployerSubscription>> getCurrentSubscription(@PathVariable String employerId) {
        EmployerSubscription result = employerSubscriptionService.getCurrentActiveSubscription(employerId)
                .orElse(null);
        
        if (result == null) {
             return ResponseEntity.ok(ApiResponse.<EmployerSubscription>builder()
                .code(Code.SUBSCRIPTION_NOT_FOUND.getCode())
                .message(Code.SUBSCRIPTION_NOT_FOUND.getMessage())
                .result(null)
                .build());
        }

        return ResponseEntity.ok(ApiResponse.<EmployerSubscription>builder()
                .code(Code.GET_SUBSCRIPTION_SUCCESS.getCode())
                .message(Code.GET_SUBSCRIPTION_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployerSubscription>>> getAllSubscriptions() {
        List<EmployerSubscription> result = employerSubscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(ApiResponse.<List<EmployerSubscription>>builder()
                .code(Code.GET_SUBSCRIPTION_SUCCESS.getCode())
                .message(Code.GET_SUBSCRIPTION_SUCCESS.getMessage())
                .result(result)
                .build());
    }
}
