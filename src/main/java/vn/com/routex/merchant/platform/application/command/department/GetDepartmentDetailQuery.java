package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record GetDepartmentDetailQuery(
        RequestContext context,
        String merchantId,
        String departmentId
) {
}
