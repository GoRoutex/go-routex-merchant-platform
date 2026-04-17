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
        String templateId,
        String vehiclePlate,
        VehicleStatus status
) {
}
