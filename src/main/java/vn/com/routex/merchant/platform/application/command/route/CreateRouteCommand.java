package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.util.List;

@Builder
public record CreateRouteCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String pickupBranch,
        String origin,
        String destination,
        String plannedStartTime,
        String plannedEndTime,
        List<RoutePointCommand> routePoints
) {
}
