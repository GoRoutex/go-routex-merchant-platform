package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.time.OffsetDateTime;

@Builder
public record UpdateTripCommand(
        RequestContext context,
        String tripId,
        String routeId,
        String merchantId,
        OffsetDateTime departureTime,
        String pickupBranch,
        String rawDepartureTime,
        String rawDepartureDate
) {
}
