package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.WardsEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WardsEntityRepository extends JpaRepository<WardsEntity, String> {
    List<WardsEntity> findByProvinceId(String provinceId);

    @Query("SELECT w FROM WardsEntity w WHERE (:provinceId IS NULL OR w.provinceId = :provinceId)")
    Page<WardsEntity> fetch(@Param("provinceId") String provinceId, Pageable pageable);

    @Query("SELECT w FROM WardsEntity w WHERE " +
           "(:provinceId IS NULL OR w.provinceId = :provinceId) AND " +
           "(LOWER(w.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<WardsEntity> search(@Param("keyword") String keyword, @Param("provinceId") String provinceId, Pageable pageable);
}
