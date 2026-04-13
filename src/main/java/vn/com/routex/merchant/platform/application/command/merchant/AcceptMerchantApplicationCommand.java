package vn.com.routex.merchant.platform.application.command.merchant;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.math.BigDecimal;

@Builder
public record AcceptMerchantApplicationCommand(
        RequestContext context,
        String applicationFormId,
        String approvedBy,
        BigDecimal commission
) {
}
