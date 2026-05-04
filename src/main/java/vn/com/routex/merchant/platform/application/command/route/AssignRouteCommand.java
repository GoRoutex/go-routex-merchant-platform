package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record AssignRouteCommand(
        String merchantId,
        String creator,
        String tripId,
        String vehicleId,
        String driverId,
        RequestContext context
) {
}
