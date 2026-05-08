package vn.com.routex.merchant.platform.application.service;


import vn.com.routex.merchant.platform.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.merchant.platform.application.command.provinces.FetchProvincesResult;
import vn.com.routex.merchant.platform.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.merchant.platform.application.command.provinces.SearchProvincesResult;

public interface ProvincesManagementService {
    SearchProvincesResult searchProvinces(SearchProvincesQuery query);

    FetchProvincesResult fetchProvinces(FetchProvincesQuery query);
}
