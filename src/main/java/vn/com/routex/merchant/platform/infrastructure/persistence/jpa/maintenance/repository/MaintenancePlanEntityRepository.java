package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.maintenance.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.maintenance.MaintenancePlanEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenancePlanEntityRepository extends JpaRepository<MaintenancePlanEntity, String> {
    Optional<MaintenancePlanEntity> findByIdAndMerchantId(String id, String merchantId);

    List<MaintenancePlanEntity> findByVehicleIdAndMerchantId(String vehicleId, String merchantId);

    boolean existsByCodeAndMerchantId(String code, String merchantId);

    @Query("""
            select mp
            from MaintenancePlanEntity mp
            where mp.merchantId = :merchantId
              and (:vehicleId is null or mp.vehicleId = :vehicleId)
              and (:status is null or mp.status = :status)
              and (:type is null or mp.type = :type)
              and (:fromPlannedDate is null or mp.plannedDate >= :fromPlannedDate)
              and (:toPlannedDate is null or mp.plannedDate <= :toPlannedDate)
            """)
    Page<MaintenancePlanEntity> findByFilters(
            @Param("merchantId") String merchantId,
            @Param("vehicleId") String vehicleId,
            @Param("status") MaintenancePlanStatus status,
            @Param("type") MaintenancePlanType type,
            @Param("fromPlannedDate") LocalDate fromPlannedDate,
            @Param("toPlannedDate") LocalDate toPlannedDate,
            Pageable pageable
    );
}
