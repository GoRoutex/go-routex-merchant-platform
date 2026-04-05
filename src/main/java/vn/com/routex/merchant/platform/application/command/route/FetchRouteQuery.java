package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

@Builder
public record FetchRouteQuery(
        String routeId,
        String requestId,
        String requestDateTime,
        String channel
) {
}
