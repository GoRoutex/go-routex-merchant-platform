package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.provinces.model.Province;
import vn.com.routex.merchant.platform.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProvincesRepositoryAdapter implements ProvincesRepositoryPort {

    private final ProvincesEntityRepository provincesEntityRepository;
    private final ProvincesPersistenceMapper provincesPersistenceMapper;

    @Override
    public Optional<Province> findById(String id) {
        return provincesEntityRepository.findById(id).map(provincesPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Province> findByCode(String code) {
        return provincesEntityRepository.findByCode(code).map(provincesPersistenceMapper::toDomain);
    }
}
