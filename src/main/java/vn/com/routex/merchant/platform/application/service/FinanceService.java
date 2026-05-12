package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.finance.FetchMerchantRevenueQuery;
import vn.com.routex.merchant.platform.application.command.finance.FetchSystemRevenueQuery;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.MerchantRevenueResponse;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.SystemRevenueResponse;

public interface FinanceService {
    MerchantRevenueResponse getMerchantRevenue(FetchMerchantRevenueQuery query);
    SystemRevenueResponse getSystemRevenue(FetchSystemRevenueQuery query);
}

