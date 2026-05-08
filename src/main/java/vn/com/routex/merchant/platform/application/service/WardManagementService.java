package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.wards.FetchWardsQuery;
import vn.com.routex.merchant.platform.application.command.wards.FetchWardsResult;
import vn.com.routex.merchant.platform.application.command.wards.SearchWardsQuery;
import vn.com.routex.merchant.platform.application.command.wards.SearchWardsResult;

public interface WardManagementService {
    FetchWardsResult fetchWards(FetchWardsQuery query);
    SearchWardsResult searchWards(SearchWardsQuery query);
}
