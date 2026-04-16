package vn.com.routex.merchant.platform.application.command.review;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchMerchantReviewDetailQuery(
        RequestContext context,
        String merchantId,
        String reviewId
) {
}
