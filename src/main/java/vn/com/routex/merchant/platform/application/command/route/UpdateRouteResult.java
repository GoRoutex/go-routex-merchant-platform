package vn.com.routex.merchant.platform.application.command.route;


import lombok.Builder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.util.List;

@Builder
public record UpdateRouteResult(
        String routeId,
        String creator,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        Long duration,
        RouteStatus status,
        List<UpdateRoutePointResult> routePoints
) {
    @Builder
    public record UpdateRoutePointResult(
            String id,
            String operationOrder,
            String note
    ) {

    }
}
