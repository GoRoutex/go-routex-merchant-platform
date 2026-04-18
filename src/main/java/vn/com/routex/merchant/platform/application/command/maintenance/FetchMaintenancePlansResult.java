package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record FetchMaintenancePlansResult(
        List<FetchMaintenancePlanItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
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

    @Builder
    public record FetchMaintenancePlanItemResult(
            String id,
            MaintenancePlanVehicleResult vehicle,
            String code,
            String title,
            MaintenancePlanType type,
            MaintenancePlanStatus status,
            LocalDate plannedDate,
            LocalDate dueDate,
            Long targetOdometerKm,
            BigDecimal estimatedCost,
            String serviceProvider
    ) {
    }
}
