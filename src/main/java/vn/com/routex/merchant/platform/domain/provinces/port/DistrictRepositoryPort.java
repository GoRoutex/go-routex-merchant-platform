package vn.com.routex.merchant.platform.domain.provinces.port;

import vn.com.routex.merchant.platform.domain.provinces.model.District;
import java.util.List;
import java.util.Optional;

public interface DistrictRepositoryPort {
    Optional<District> findById(Integer id);
    List<District> findByProvinceId(Integer provinceId);
}
