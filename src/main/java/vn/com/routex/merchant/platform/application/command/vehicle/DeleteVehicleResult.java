package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;

@Builder
public record DeleteVehicleResult(
        String id,
        VehicleStatus status
) {
}

