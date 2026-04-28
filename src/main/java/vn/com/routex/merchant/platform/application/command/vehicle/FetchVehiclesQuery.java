package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;

@Builder
public record FetchVehiclesQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        VehicleStatus status,
        RequestContext context
) {
}
