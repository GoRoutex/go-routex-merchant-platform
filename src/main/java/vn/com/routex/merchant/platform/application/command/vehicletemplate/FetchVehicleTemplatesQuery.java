package vn.com.routex.merchant.platform.application.command.vehicletemplate;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

@Builder
public record FetchVehicleTemplatesQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        VehicleTemplateStatus status,
        VehicleTemplateCategory category,
        VehicleTemplateType type,
        RequestContext context
) {
}
