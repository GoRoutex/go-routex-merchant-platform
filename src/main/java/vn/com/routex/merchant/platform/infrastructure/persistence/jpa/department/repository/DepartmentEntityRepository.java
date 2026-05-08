package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.department.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.department.entity.DepartmentEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByIdAndMerchantId(String id, String merchantId);

    Optional<DepartmentEntity> findByNameIgnoreCaseAndMerchantId(String name, String merchantId);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndMerchantId(String name, String merchantId);

    List<DepartmentEntity> findByMerchantId(String merchantId);

    Page<DepartmentEntity> findByMerchantId(String merchantId, Pageable pageable);
}
