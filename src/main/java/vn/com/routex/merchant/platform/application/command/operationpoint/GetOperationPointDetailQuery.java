package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record GetOperationPointDetailQuery(
        RequestContext context,
        String merchantId,
        String operationPointId,
        String code,
        String name
) {
}
