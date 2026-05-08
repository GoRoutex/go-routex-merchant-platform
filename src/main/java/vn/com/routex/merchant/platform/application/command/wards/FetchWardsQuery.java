package vn.com.routex.merchant.platform.application.command.wards;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchWardsQuery(
        String provinceId,
        String pageSize,
        String pageNumber,
        RequestContext context
) {
}
