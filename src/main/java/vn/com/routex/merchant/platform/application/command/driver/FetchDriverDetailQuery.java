package vn.com.routex.merchant.platform.application.command.driver;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchDriverDetailQuery(
        RequestContext context,
        String merchantId,
        String driverId,
        String userId,
        String employeeCode
) {
}
