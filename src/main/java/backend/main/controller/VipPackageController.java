package backend.main.controller;

import java.util.List;

import backend.main.enums.Code;
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
public class     VipPackageController {

    private final VipPackageService vipPackageService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VipPackage>> createVipPackage(@RequestBody VipPackageRequest request) {
        VipPackage result = vipPackageService.createVipPackage(request);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code(Code.CREATE_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.CREATE_VIP_PACKAGE_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<VipPackage>> updateVipPackage(@PathVariable Integer id, @RequestBody VipPackageRequest request) {
        VipPackage result = vipPackageService.updateVipPackage(id, request);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code(Code.UPDATE_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.UPDATE_VIP_PACKAGE_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteVipPackage(@PathVariable Integer id) {
        vipPackageService.deleteVipPackage(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(Code.DELETE_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.DELETE_VIP_PACKAGE_SUCCESS.getMessage())
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<VipPackage>> getVipPackageById(@PathVariable Integer id) {
        VipPackage result = vipPackageService.getVipPackageById(id);
        return ResponseEntity.ok(ApiResponse.<VipPackage>builder()
                .code(Code.GET_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.GET_VIP_PACKAGE_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<List<VipPackage>>> getAllVipPackages() {
        List<VipPackage> result = vipPackageService.getAllVipPackages();
        return ResponseEntity.ok(ApiResponse.<List<VipPackage>>builder()
                .code(Code.GET_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.GET_VIP_PACKAGE_SUCCESS.getMessage())
                .result(result)
                .build());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYER')")
    public ResponseEntity<ApiResponse<List<VipPackage>>> getActiveVipPackages() {
        List<VipPackage> result = vipPackageService.getActiveVipPackages();
        return ResponseEntity.ok(ApiResponse.<List<VipPackage>>builder()
                .code(Code.GET_VIP_PACKAGE_SUCCESS.getCode())
                .message(Code.GET_VIP_PACKAGE_SUCCESS.getMessage())
                .result(result)
                .build());
    }
}
