package vn.com.routex.merchant.platform.application.command.route;


import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchDetailRouteQuery(
        RequestContext context,
        String routeId,
        String merchantId
) {
}
