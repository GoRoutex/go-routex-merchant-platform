package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleType;

@Builder
public record FetchVehicleDetailResult(
        String id,
        String merchantId,
        String creator,
        VehicleStatus status,
        VehicleType type,
        String vehiclePlate,
        Integer seatCapacity,
        Boolean hasFloor,
        String manufacturer
) {
}
