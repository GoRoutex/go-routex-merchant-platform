package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

@Builder
public record FetchVehicleDetailResult(
        String id,
        String merchantId,
        String templateId,
        String creator,
        VehicleStatus status,
        VehicleTemplateCategory category,
        VehicleTemplateType type,
        String vehiclePlate,
        Long seatCapacity,
        Boolean hasFloor,
        String manufacturer
) {
}
