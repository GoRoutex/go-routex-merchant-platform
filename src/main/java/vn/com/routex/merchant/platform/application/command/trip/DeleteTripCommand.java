package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteTripCommand(
        RequestContext context,
        String tripId,
        String merchantId
) {
}
