package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteDepartmentCommand(
        RequestContext context,
        String merchantId,
        String id
) {
}
