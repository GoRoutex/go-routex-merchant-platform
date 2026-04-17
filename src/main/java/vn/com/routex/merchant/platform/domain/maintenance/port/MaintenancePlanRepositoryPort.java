package vn.com.routex.merchant.platform.domain.maintenance.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.domain.maintenance.model.MaintenancePlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenancePlanRepositoryPort {
    Optional<MaintenancePlan> findById(String id);

    Optional<MaintenancePlan> findById(String id, String merchantId);

    List<MaintenancePlan> findByVehicleId(String vehicleId, String merchantId);

    boolean existsByCode(String code, String merchantId);

    void save(MaintenancePlan maintenancePlan);

    PagedResult<MaintenancePlan> fetch(
            String merchantId,
            String vehicleId,
            MaintenancePlanStatus status,
            MaintenancePlanType type,
            LocalDate fromPlannedDate,
            LocalDate toPlannedDate,
            int pageNumber,
            int pageSize
    );
}
