package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantCommand;
import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantResult;

public interface MerchantAccessService {
    GetMyMerchantResult fetchMerchantDetail(GetMyMerchantCommand build);
}
