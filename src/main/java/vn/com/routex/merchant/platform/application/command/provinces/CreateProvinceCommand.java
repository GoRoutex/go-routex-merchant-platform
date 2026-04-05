package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record CreateProvinceCommand(
        RequestContext context,
        String name,
        String code
) {
}

