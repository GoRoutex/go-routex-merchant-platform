package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.provinces.model.Ward;
import vn.com.routex.merchant.platform.domain.provinces.port.WardRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.repository.WardsEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WardRepositoryAdapter implements WardRepositoryPort {

    private final WardsEntityRepository repository;
    private final AdministrativePersistenceMapper mapper;

    @Override
    public Optional<Ward> findById(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Ward> findByDistrictId(Integer districtId) {
        return repository.findByDistrictId(districtId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
