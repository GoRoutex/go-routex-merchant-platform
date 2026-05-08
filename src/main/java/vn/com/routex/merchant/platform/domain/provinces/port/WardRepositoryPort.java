package vn.com.routex.merchant.platform.domain.provinces.port;

import vn.com.routex.merchant.platform.domain.provinces.model.Ward;
import java.util.List;
import java.util.Optional;

import vn.com.routex.merchant.platform.domain.common.PagedResult;

public interface WardRepositoryPort {
    Optional<Ward> findById(String id);
    List<Ward> findByProvinceId(String provinceId);
    PagedResult<Ward> fetch(String provinceId, int pageNumber, int pageSize);
    PagedResult<Ward> search(String keyword, String provinceId, int page, int size);
}
