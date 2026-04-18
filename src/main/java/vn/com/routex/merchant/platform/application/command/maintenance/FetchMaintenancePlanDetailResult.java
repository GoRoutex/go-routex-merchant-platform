package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record FetchMaintenancePlanDetailResult(
        String id,
        String merchantId,
        MaintenancePlanVehicleResult vehicle,
        String code,
        String title,
        String description,
        MaintenancePlanType type,
        MaintenancePlanStatus status,
        LocalDate plannedDate,
        LocalDate dueDate,
        LocalDate completedDate,
        Long currentOdometerKm,
        Long targetOdometerKm,
        BigDecimal estimatedCost,
        BigDecimal actualCost,
        String serviceProvider,
        String note
) {
    @Builder
    public record MaintenancePlanVehicleResult(
            String id,
            String templateId,
            VehicleStatus status,
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            String vehiclePlate,
            Long seatCapacity,
            Boolean hasFloor,
            String manufacturer
    ) {
    }
}
