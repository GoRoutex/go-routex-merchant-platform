package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchDepartmentQuery(
        String pageSize,
        String pageNumber,
        String merchantId,
        String provinceId,
        RequestContext context
) {
}
