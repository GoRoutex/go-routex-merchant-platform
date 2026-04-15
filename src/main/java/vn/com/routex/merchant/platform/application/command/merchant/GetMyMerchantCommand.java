package vn.com.routex.merchant.platform.application.command.merchant;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record GetMyMerchantCommand(
    RequestContext context,
    String merchantId
) {
}
