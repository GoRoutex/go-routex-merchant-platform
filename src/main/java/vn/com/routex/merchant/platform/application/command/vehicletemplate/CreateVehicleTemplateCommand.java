package vn.com.routex.merchant.platform.application.command.vehicletemplate;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.vehicle.FuelType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

import java.math.BigDecimal;

@Builder
public record CreateVehicleTemplateCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String code,
        String name,
        String manufacturer,
        String model,
        Long seatCapacity,
        VehicleTemplateCategory category,
        VehicleTemplateType type,
        FuelType fuelType,
        Boolean hasFloor,
        BigDecimal ticketPrice,
        VehicleTemplateStatus status
) {
}
