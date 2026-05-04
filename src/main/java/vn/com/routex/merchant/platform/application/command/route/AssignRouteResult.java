package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

@Builder
public record AssignRouteResult(
        String creator,
        String tripId,
        String vehicleId,
        String driverId,
        String assignedAt,
        String status
) {
}
