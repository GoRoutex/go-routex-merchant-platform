package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CreateMaintenancePlanCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String vehicleId,
        String code,
        String title,
        String description,
        MaintenancePlanType type,
        LocalDate plannedDate,
        LocalDate dueDate,
        Long currentOdometerKm,
        Long targetOdometerKm,
        BigDecimal estimatedCost,
        String serviceProvider,
        String note
) {
}
