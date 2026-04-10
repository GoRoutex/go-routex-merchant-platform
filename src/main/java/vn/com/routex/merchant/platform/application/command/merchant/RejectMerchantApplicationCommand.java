package vn.com.routex.merchant.platform.application.command.merchant;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record RejectMerchantApplicationCommand(
        RequestContext context,
        String applicationFormId,
        String rejectedBy,
        String rejectionReason
) {
}
