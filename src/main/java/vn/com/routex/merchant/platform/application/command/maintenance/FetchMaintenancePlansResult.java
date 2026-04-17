package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;

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
    public record FetchMaintenancePlanItemResult(
            String id,
            String vehicleId,
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
