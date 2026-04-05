package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleType;

@Builder
public record UpdateVehicleResult(
        String id,
        String creator,
        VehicleType type,
        String vehiclePlate,
        Integer seatCapacity,
        Boolean hasFloor,
        String manufacturer,
        VehicleStatus status
) {
}

