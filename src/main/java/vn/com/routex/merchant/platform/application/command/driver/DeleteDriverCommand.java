package vn.com.routex.merchant.platform.application.command.driver;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteDriverCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String driverId
) {
}
