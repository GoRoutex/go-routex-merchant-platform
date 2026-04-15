package vn.com.routex.merchant.platform.domain.provinces.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.merchant.platform.domain.provinces.readmodel.ProvincesSearchItem;

import java.util.List;

public interface ProvincesQueryPort {
    List<ProvincesSearchItem> search(String merchantId, String keyword, int page, int size);

    PagedResult<ProvincesFetchView> fetchRoutes(String merchantId, int pageNumber, int pageSize);

    PagedResult<ProvincesFetchView> fetchMasterProvinces(int pageNumber, int pageSize);
}
