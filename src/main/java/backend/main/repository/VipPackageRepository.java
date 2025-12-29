package backend.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.main.entities.VipPackage;

@Repository
public interface VipPackageRepository extends JpaRepository<VipPackage, Integer> {
    List<VipPackage> findByIsActiveTrue();
}
