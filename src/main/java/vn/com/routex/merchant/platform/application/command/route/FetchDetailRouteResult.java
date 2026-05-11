package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.util.List;


@Builder
public record FetchDetailRouteResult(
        String id,
        String creator,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        String originProvinceId,
        String destinationProvinceId,
        String originDepartmentId,
        String originDepartmentName,
        String destinationDepartmentId,
        String destinationDepartmentName,
        RouteStatus status,
        Long duration,
        List<RoutePointResult> routePoints
) {
}
