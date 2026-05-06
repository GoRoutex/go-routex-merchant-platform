package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.WardsEntity;
import java.util.List;

public interface WardsEntityRepository extends JpaRepository<WardsEntity, Integer> {
    List<WardsEntity> findByDistrictId(int districtId);
}
