package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchRoutesQuery(
        RequestContext context,
        String pageSize,
        String pageNumber,
        String merchantId,
        String merchantName
) {
}
