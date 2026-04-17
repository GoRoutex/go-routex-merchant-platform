package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;

@Builder
public record DeleteMaintenancePlanResult(
        String id,
        String code,
        MaintenancePlanStatus status
) {
}
