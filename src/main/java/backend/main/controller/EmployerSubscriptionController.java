package backend.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.EmployerSubscriptionRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.EmployerSubscription;
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
                .code("200")
                .message("Create Subscription successfully")
                .result(result)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployerSubscription>> getSubscriptionById(@PathVariable Integer id) {
        EmployerSubscription result = employerSubscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(ApiResponse.<EmployerSubscription>builder()
                .code("200")
                .message("Get Subscription successfully")
                .result(result)
                .build());
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<ApiResponse<List<EmployerSubscription>>> getSubscriptionsByEmployerId(@PathVariable String employerId) {
        List<EmployerSubscription> result = employerSubscriptionService.getSubscriptionsByEmployerId(employerId);
        return ResponseEntity.ok(ApiResponse.<List<EmployerSubscription>>builder()
                .code("200")
                .message("Get Subscriptions by Employer successfully")
                .result(result)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployerSubscription>>> getAllSubscriptions() {
        List<EmployerSubscription> result = employerSubscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(ApiResponse.<List<EmployerSubscription>>builder()
                .code("200")
                .message("Get all Subscriptions successfully")
                .result(result)
                .build());
    }
}
