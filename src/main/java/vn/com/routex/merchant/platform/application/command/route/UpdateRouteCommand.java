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
        String originName,
        String destinationName,
        RouteStatus status,
        List<UpdateRoutePointCommand> routePoints
) {

    @Builder
    public record UpdateRoutePointCommand(
        String id,
        String operationOrder,
        String note
    ) {
    }
}
