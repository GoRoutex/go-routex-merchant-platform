package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record UpdateRouteCommand(
        RequestContext context,
        String routeId,
        String creator,
        String pickupBranch,
        String origin,
        String destination,
        OffsetDateTime plannedStartTime,
        OffsetDateTime plannedEndTime,
        OffsetDateTime actualStartTime,
        OffsetDateTime actualEndTime,
        RouteStatus status,
        List<UpdateRoutePointCommand> routePoints
) {

    @Builder
    public record UpdateRoutePointCommand(
        String id,
        String operationOrder,
        OffsetDateTime plannedArrivalTime,
        OffsetDateTime plannedDepartureTime,
        String note
    ) {
    }
}
