package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.dashboard.FetchMerchantDashboardQuery;
import vn.com.routex.merchant.platform.interfaces.model.dashboard.response.MerchantDashboardResponse;

public interface DashboardService {
    MerchantDashboardResponse getDashboard(FetchMerchantDashboardQuery query);
}
