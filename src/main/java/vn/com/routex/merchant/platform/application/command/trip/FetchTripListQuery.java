package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;

@Builder
public record FetchTripListQuery(
        RequestContext context,
        TripStatus status,
        String pageSize,
        String pageNumber
) {
}
