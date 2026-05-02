package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.time.OffsetDateTime;

@Builder
public record CreateTripCommand(
        RequestContext context,
        String routeId,
        String merchantId,
        OffsetDateTime departureTime,
        String rawDepartureTime,
        String rawDepartureDate,
        Long durationMinutes
) {
}
