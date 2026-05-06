package vn.com.routex.merchant.platform.domain.provinces.port;

import vn.com.routex.merchant.platform.domain.provinces.model.Ward;
import java.util.List;
import java.util.Optional;

public interface WardRepositoryPort {
    Optional<Ward> findById(Integer id);
    List<Ward> findByDistrictId(Integer districtId);
}
