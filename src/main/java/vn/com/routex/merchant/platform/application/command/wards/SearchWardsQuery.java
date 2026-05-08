package vn.com.routex.merchant.platform.application.command.wards;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record SearchWardsQuery(
        String keyword,
        String provinceId,
        int page,
        int size,
        RequestContext context
) {
}
