package vn.com.routex.merchant.platform.domain.provinces.port;


import vn.com.routex.merchant.platform.domain.provinces.model.Province;

import java.util.Optional;

public interface ProvincesRepositoryPort {
    Optional<Province> findById(Integer id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Province save(Province province);

    void deleteById(Integer id);
}

