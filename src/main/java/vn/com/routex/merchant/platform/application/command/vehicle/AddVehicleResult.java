package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleType;

@Builder
public record AddVehicleResult(
        String id,
        String templateId,
        String creator,
        VehicleTemplateType type,
        VehicleTemplateCategory category,
        String vehiclePlate,
        Long seatCapacity,
        String manufacturer,
        VehicleStatus status
) {
}
