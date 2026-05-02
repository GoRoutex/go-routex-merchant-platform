package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record FetchRouteResult(
        String id,
        String creator,
        String originCode,
        String originName,
        String destinationCode,
        String destinationName,
        String status,
        Long availableSeats,
        String vehicleId,
        String vehiclePlate,
        Boolean hasFloor,
        OffsetDateTime assignedAt,
        AssignmentRecord assignmentRecord,
        List<RoutePointResult> routePoints
) {


    @Builder
    public record AssignmentRecord(
            String vehicleId,
            String vehiclePlate,
            String vehicleTemplateName,
            String driverId,
            String driverName
    ) {}
}
