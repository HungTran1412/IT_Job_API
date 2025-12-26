package backend.main.services;

import java.util.List;

import backend.main.dto.request.VipPackageRequest;
import backend.main.entities.VipPackage;

public interface VipPackageService {
    VipPackage createVipPackage(VipPackageRequest request);
    VipPackage updateVipPackage(Integer id, VipPackageRequest request);
    void deleteVipPackage(Integer id);
    VipPackage getVipPackageById(Integer id);
    List<VipPackage> getAllVipPackages();
}
