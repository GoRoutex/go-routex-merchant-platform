package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteOperationPointCommand(
        RequestContext context,
        String id
) {
}
