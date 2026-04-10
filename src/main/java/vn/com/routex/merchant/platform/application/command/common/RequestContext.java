package vn.com.routex.merchant.platform.application.command.common;

import lombok.Builder;

@Builder
public record RequestContext(
        String requestId,
        String requestDateTime,
        String channel,
        String merchantId
) {
}

