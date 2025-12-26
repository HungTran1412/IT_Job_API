package backend.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.dto.request.VipPackageRequest;
import backend.main.dto.response.ApiResponse;
import backend.main.entities.VipPackage;
import backend.main.services.VipPackageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vip-packages")
@RequiredArgsConstructor
public class VipPackageController {

    private final VipPackageService vipPackageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VipPackage>> createVipPackage(@RequestBody VipPackageRequest request) {
        VipPackage result = vipPackageService.createVipPackage(request);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code("200")
                .message("Create Vip Package successfully")
                .result(result)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VipPackage>> updateVipPackage(@PathVariable String id, @RequestBody VipPackageRequest request) {
        VipPackage result = vipPackageService.updateVipPackage(id, request);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code("200")
                .message("Update Vip Package successfully")
                .result(result)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVipPackage(@PathVariable String id) {
        vipPackageService.deleteVipPackage(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code("204")
                .message("Delete Vip Package successfully")
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    public ResponseEntity<ApiResponse<VipPackage>> getVipPackageById(@PathVariable String id) {
        VipPackage result = vipPackageService.getVipPackageById(id);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code("200")
                .message("Get Vip Package successfully")
                .result(result)
                .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    public ResponseEntity<ApiResponse<List<VipPackage>>> getAllVipPackages() {
        List<VipPackage> result = vipPackageService.getAllVipPackages();
        return ResponseEntity.ok(ApiResponse.<List<VipPackage>>builder()
                .code("200")
                .message("Get all Vip Packages successfully")
                .result(result)
                .build());
    }
}
