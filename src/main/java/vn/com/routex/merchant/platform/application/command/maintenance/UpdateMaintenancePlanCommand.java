package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UpdateMaintenancePlanCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String maintenancePlanId,
        String vehicleId,
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
}
