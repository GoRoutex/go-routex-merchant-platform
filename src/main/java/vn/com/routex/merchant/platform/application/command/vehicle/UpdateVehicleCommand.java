package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;

@Builder
public record UpdateVehicleCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String vehicleId,
        String type,
        String vehiclePlate,
        String seatCapacity,
        String manufacturer,
        Boolean hasFloor,
        VehicleStatus status
) {
}
