package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

import java.time.OffsetDateTime;

@Builder
public record FetchTripDetailResult(
        String tripId,
        String creator,
        String tripCode,
        String pickupBranch,
        OffsetDateTime departureTime,
        String rawDepartureTime,
        String rawDepartureDate,
        String rawArrivalTime,
        TripStatus status,
        FetchTripDetailRoute route
) {

    @Builder
    public record FetchTripDetailRoute(
            String routeId,
            String originCode,
            String originName,
            String destinationCode,
            String destinationName,
            String originDepartmentId,
            String destinationDepartmentId,
            Long duration
    ) {}
}
