package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchVehiclesQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        RequestContext context
) {
}
