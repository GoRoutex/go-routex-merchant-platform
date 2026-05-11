package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.util.List;

@Builder
public record UpdateRouteCommand(
        RequestContext context,
        String routeId,
        String creator,
        String originName,
        String destinationName,
        String originDepartmentId,
        String destinationDepartmentId,
        RouteStatus status,
        Long duration,
        List<UpdateRoutePointCommand> routePoints
) {

    @Builder
    public record UpdateRoutePointCommand(
        int stopOrder,
        String note,
        String departmentId,
        String stopName,
        String stopAddress,
        String stopCity,
        Double stopLatitude,
        Double stopLongitude,
        Integer timeAtDepartment
    ) {
    }
}
