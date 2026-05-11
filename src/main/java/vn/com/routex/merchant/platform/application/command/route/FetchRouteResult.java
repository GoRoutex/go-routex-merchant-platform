package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.util.List;

@Builder
public record FetchRouteResult(
        String id,
        String creator,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        String originDepartmentId,
        String originDepartmentName,
        String destinationDepartmentId,
        String destinationDepartmentName,
        Long duration,
        RouteStatus status,
        List<RoutePointResult> routePoints
) {
}
