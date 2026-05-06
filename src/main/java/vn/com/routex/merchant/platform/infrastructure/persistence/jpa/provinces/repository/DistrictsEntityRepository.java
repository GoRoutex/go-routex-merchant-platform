package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.DistrictsEntity;
import java.util.List;

public interface DistrictsEntityRepository extends JpaRepository<DistrictsEntity, Integer> {
    List<DistrictsEntity> findByProvinceId(int provinceId);
}
