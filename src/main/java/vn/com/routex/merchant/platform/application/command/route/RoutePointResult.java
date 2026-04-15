package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record RoutePointResult(
        String id,
        String operationOrder,
        String routeId,
        OffsetDateTime plannedArrivalTime,
        OffsetDateTime plannedDepartureTime,
        String note,
        String operationPointId,
        String stopName,
        String stopAddress,
        String stopCity,
        Double stopLatitude,
        Double stopLongitude
) {
}
