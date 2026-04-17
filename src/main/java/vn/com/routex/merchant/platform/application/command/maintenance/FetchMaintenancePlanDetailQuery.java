package vn.com.routex.merchant.platform.application.command.maintenance;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchMaintenancePlanDetailQuery(
        RequestContext context,
        String merchantId,
        String maintenancePlanId
) {
}
