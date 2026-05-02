package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

import java.time.OffsetDateTime;

@Builder
public record UpdateTripResult(
        String tripId,
        String routeId,
        String merchantId,
        OffsetDateTime departureTime,
        String rawDepartureTime,
        String rawDepartureDate,
        Long durationMinutes,
        TripStatus status
) {
}
