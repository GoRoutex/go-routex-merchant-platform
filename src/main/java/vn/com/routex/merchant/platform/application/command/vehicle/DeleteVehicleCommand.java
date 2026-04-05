package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;

@Builder
public record DeleteVehicleCommand(
        String creator,
        String vehicleId,
        String requestId,
        String requestDateTime,
        String channel
) {
}

