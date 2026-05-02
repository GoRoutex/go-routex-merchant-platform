package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

import java.time.OffsetDateTime;

@Builder
public record FetchTripDetailResult(
        String tripId,
        String routeId,
        String merchantId,
        String tripCode,
        OffsetDateTime departureTime,
        String rawDepartureTime,
        String rawDepartureDate,
        Long durationMinutes,
        TripStatus status
) {
}
