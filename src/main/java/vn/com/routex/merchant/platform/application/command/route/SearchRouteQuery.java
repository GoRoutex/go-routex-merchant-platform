package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record SearchRouteQuery(
        RequestContext context,
        String merchantId,
        String origin,
        String destination,
        String departureDate,
        String seat,
        String fromTime,
        String toTime,
        String pageSize,
        String pageNumber
) {
}
