package vn.com.routex.merchant.platform.application.command.finance;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.time.OffsetDateTime;

@Builder
public record FetchMerchantRevenueQuery(
        String merchantId,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        RequestContext context
) {}
