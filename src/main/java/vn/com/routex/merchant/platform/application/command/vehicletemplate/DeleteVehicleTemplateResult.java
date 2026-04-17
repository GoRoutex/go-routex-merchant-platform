package vn.com.routex.merchant.platform.application.command.vehicletemplate;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;

@Builder
public record DeleteVehicleTemplateResult(
        String id,
        String code,
        VehicleTemplateStatus status
) {
}
