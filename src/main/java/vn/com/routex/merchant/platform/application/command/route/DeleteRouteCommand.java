package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteRouteCommand(
        RequestContext context,
        String creator,
        String routeId,
        String merchantId
) {
}
