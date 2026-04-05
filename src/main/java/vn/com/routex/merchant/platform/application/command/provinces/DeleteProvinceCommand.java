package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteProvinceCommand(
        RequestContext context,
        Integer id
) {
}

