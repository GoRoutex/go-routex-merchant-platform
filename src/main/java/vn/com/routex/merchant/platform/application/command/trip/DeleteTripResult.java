package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

@Builder
public record DeleteTripResult(
        String tripId,
        TripStatus status
) {
}
