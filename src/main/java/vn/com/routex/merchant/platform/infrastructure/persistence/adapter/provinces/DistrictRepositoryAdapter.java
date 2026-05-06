package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.provinces.model.District;
import vn.com.routex.merchant.platform.domain.provinces.port.DistrictRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository.DistrictsEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DistrictRepositoryAdapter implements DistrictRepositoryPort {

    private final DistrictsEntityRepository districtEntityRepository;
    private final AdministrativePersistenceMapper mapper;

    @Override
    public Optional<District> findById(Integer id) {
        return districtEntityRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<District> findByProvinceId(Integer provinceId) {
        return districtEntityRepository.findByProvinceId(provinceId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
