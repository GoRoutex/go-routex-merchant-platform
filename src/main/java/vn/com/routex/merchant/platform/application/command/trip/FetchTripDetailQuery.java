package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

@Builder
public record FetchTripDetailQuery(
        String tripId,
        TripStatus status
) {
}
