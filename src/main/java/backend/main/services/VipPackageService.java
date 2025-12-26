package backend.main.services;

import java.util.List;

import backend.main.dto.request.VipPackageRequest;
import backend.main.entities.VipPackage;

public interface VipPackageService {
    VipPackage createVipPackage(VipPackageRequest request);
    VipPackage updateVipPackage(String id, VipPackageRequest request);
    void deleteVipPackage(String id);
    VipPackage getVipPackageById(String id);
    List<VipPackage> getAllVipPackages();
}
