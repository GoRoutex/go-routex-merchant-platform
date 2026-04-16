package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.driver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.driver.entity.DriverProfileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileEntityRepository extends JpaRepository<DriverProfileEntity, String> {
    Optional<DriverProfileEntity> findByUserId(String userId);

    Optional<DriverProfileEntity> findByIdAndMerchantId(String id, String merchantId);

    Optional<DriverProfileEntity> findByUserIdAndMerchantId(String userId, String merchantId);

    Optional<DriverProfileEntity> findByEmployeeCodeAndMerchantId(String employeeCode, String merchantId);

    boolean existsByUserIdAndMerchantId(String userId, String merchantId);

    boolean existsByEmployeeCodeAndMerchantId(String employeeCode, String merchantId);

    List<DriverProfileEntity> findByMerchantId(String merchantId);

    Page<DriverProfileEntity> findByMerchantId(String merchantId, Pageable pageable);
}
