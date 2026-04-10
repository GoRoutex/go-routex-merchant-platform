package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record AddVehicleCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String type,
        String vehiclePlate,
        String seatCapacity,
        String manufacturer
) {
}
