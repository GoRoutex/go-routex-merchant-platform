package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

@Builder
public record RoutePointResult(
        String id,
        int operationOrder,
        String routeId,
        String note,
        String operationPointId,
        String stopName,
        String stopAddress,
        String stopCity,
        Double stopLatitude,
        Double stopLongitude
) {
}
