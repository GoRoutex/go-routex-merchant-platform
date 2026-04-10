package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchOperationPointQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        RequestContext context
) {
}
