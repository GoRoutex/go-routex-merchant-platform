package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

@Builder
public record UpdateVehicleResult(
        String id,
        String templateId,
        String creator,
        VehicleTemplateCategory category,
        VehicleTemplateType type,
        String vehiclePlate,
        Long seatCapacity,
        Boolean hasFloor,
        String manufacturer,
        VehicleStatus status
) {
}
