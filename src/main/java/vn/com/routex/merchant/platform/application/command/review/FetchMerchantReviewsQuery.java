package vn.com.routex.merchant.platform.application.command.review;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchMerchantReviewsQuery(
        RequestContext context,
        String merchantId,
        String pageNumber,
        String pageSize
) {
}
