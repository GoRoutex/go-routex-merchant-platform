package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;

@Builder
public record FetchRoutesQuery(
        RequestContext context,
        RouteStatus status,
        String pageSize,
        String pageNumber,
        String merchantId,
        String merchantName
) {
}
