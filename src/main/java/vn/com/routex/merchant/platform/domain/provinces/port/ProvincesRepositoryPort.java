package vn.com.routex.merchant.platform.domain.provinces.port;


import vn.com.routex.merchant.platform.domain.provinces.model.Province;

import java.util.Optional;

public interface ProvincesRepositoryPort {
    // Master data
    Optional<Province> findById(String id);

    Optional<Province> findByCode(String code);
}

