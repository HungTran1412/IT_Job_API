package backend.main.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.main.dto.request.VipPackageRequest;
import backend.main.entities.VipPackage;
import backend.main.repository.VipPackageRepository;
import backend.main.services.VipPackageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VipPackageServiceImpl implements VipPackageService {

    private final VipPackageRepository vipPackageRepository;

    @Override
    public VipPackage createVipPackage(VipPackageRequest request) {
        VipPackage vipPackage = VipPackage.builder()
                .name(request.getName())
                .price(request.getPrice())
                .durationDays(request.getDurationDays())
                .postLimit(request.getPostLimit())
                .jobPostDurationDays(request.getJobPostDurationDays())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        return vipPackageRepository.save(vipPackage);
    }

    @Override
    public VipPackage updateVipPackage(String id, VipPackageRequest request) {
        VipPackage vipPackage = getVipPackageById(id);
        
        if (request.getName() != null) vipPackage.setName(request.getName());
        if (request.getPrice() != null) vipPackage.setPrice(request.getPrice());
        if (request.getDurationDays() != null) vipPackage.setDurationDays(request.getDurationDays());
        if (request.getPostLimit() != null) vipPackage.setPostLimit(request.getPostLimit());
        if (request.getJobPostDurationDays() != null) vipPackage.setJobPostDurationDays(request.getJobPostDurationDays());
        if (request.getDescription() != null) vipPackage.setDescription(request.getDescription());
        if (request.getIsActive() != null) vipPackage.setIsActive(request.getIsActive());

        return vipPackageRepository.save(vipPackage);
    }

    @Override
    public void deleteVipPackage(String id) {
        if (!vipPackageRepository.existsById(id)) {
            throw new RuntimeException("Vip Package not found");
        }
        vipPackageRepository.deleteById(id);
    }

    @Override
    public VipPackage getVipPackageById(String id) {
        return vipPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vip Package not found"));
    }

    @Override
    public List<VipPackage> getAllVipPackages() {
        return vipPackageRepository.findAll();
    }
}
