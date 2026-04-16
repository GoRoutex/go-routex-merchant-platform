package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

import java.time.OffsetDateTime;
import java.util.List;


@Builder
public record FetchDetailRouteResult(
        String id,
        String creator,
        String pickupBranch,
        String routeCode,
        String origin,
        String destination,
        OffsetDateTime plannedStartTime,
        OffsetDateTime plannedEndTime,
        OffsetDateTime actualStartTime,
        OffsetDateTime actualEndTime,
        RouteStatus status,
        Long availableSeats,
        String vehicleId,
        String vehiclePlate,
        Boolean hasFloor,
        OffsetDateTime assignedAt,
        List<RoutePointResult> routePoints
) {
}
