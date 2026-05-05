package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.util.List;

@Builder
public record CreateRouteResult(
        String id,
        String creator,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        RouteStatus status,
        Long duration,
        List<RoutePointCommand> routePoints
) {
}
