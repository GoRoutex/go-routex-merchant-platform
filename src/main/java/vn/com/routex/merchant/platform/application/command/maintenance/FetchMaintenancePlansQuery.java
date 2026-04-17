package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;

import java.time.LocalDate;

@Builder
public record FetchMaintenancePlansQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        String vehicleId,
        MaintenancePlanStatus status,
        MaintenancePlanType type,
        LocalDate fromPlannedDate,
        LocalDate toPlannedDate,
        RequestContext context
) {
}
