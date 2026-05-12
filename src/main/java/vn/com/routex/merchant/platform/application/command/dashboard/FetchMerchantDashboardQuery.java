package vn.com.routex.merchant.platform.application.command.dashboard;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record FetchMerchantDashboardQuery(
        String merchantId,
        String filterType, // DAY, WEEK, MONTH, YEAR
        RequestContext context
) {}
