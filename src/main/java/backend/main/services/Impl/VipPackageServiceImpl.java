package backend.main.services.Impl;

import java.util.List;

import backend.main.enums.Code;
import backend.main.exception.AppException;
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
                .code(request.getCode())
                .name(request.getName())
                .price(request.getPrice())
                .durationDays(request.getDurationDays())
                .postLimit(request.getPostLimit())
                .weeklyPostLimit(request.getWeeklyPostLimit())
                .jobPostDurationDays(request.getJobPostDurationDays())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        return vipPackageRepository.save(vipPackage);
    }

    @Override
    public VipPackage updateVipPackage(Integer id, VipPackageRequest request) {
        VipPackage vipPackage = getVipPackageById(id);
        
        if (request.getCode() != null) vipPackage.setCode(request.getCode());
        if (request.getName() != null) vipPackage.setName(request.getName());
        if (request.getPrice() != null) vipPackage.setPrice(request.getPrice());
        if (request.getDurationDays() != null) vipPackage.setDurationDays(request.getDurationDays());
        if (request.getPostLimit() != null) vipPackage.setPostLimit(request.getPostLimit());
        if (request.getWeeklyPostLimit() != null) vipPackage.setWeeklyPostLimit(request.getWeeklyPostLimit());
        if (request.getJobPostDurationDays() != null) vipPackage.setJobPostDurationDays(request.getJobPostDurationDays());
        if (request.getDescription() != null) vipPackage.setDescription(request.getDescription());
        if (request.getIsActive() != null) vipPackage.setIsActive(request.getIsActive());

        return vipPackageRepository.save(vipPackage);
    }

    @Override
    public void deleteVipPackage(Integer id) {
        if (!vipPackageRepository.existsById(id)) {
            throw new AppException(Code.VIP_PACKAGE_NOT_FOUND);
        }
        vipPackageRepository.deleteById(id);
    }

    @Override
    public VipPackage getVipPackageById(Integer id) {
        return vipPackageRepository.findById(id)
                .orElseThrow(() -> new AppException(Code.VIP_PACKAGE_NOT_FOUND));
    }

    @Override
    public List<VipPackage> getAllVipPackages() {
        return vipPackageRepository.findAll();
    }

    @Override
    public List<VipPackage> getActiveVipPackages() {
        return vipPackageRepository.findByIsActiveTrue();
    }
}
