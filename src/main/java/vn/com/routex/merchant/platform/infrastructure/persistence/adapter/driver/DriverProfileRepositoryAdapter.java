package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.driver;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.driver.model.DriverProfile;
import vn.com.routex.merchant.platform.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.driver.entity.DriverProfileEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.driver.repository.DriverProfileEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriverProfileRepositoryAdapter implements DriverProfileRepositoryPort {
    private final DriverProfileEntityRepository driverProfileEntityRepository;

    @Override
    public Optional<DriverProfile> findById(String id) {
        return driverProfileEntityRepository.findById(id).map(DriverProfilePersistenceMapper::toDomain);
    }

    @Override
    public Optional<DriverProfile> findById(String id, String merchantId) {
        return driverProfileEntityRepository.findByIdAndMerchantId(id, merchantId)
                .map(DriverProfilePersistenceMapper::toDomain);
    }

    @Override
    public Optional<DriverProfile> findByUserId(String userId) {
        return driverProfileEntityRepository.findByUserId(userId).map(DriverProfilePersistenceMapper::toDomain);
    }

    @Override
    public Optional<DriverProfile> findByUserId(String userId, String merchantId) {
        return driverProfileEntityRepository.findByUserIdAndMerchantId(userId, merchantId)
                .map(DriverProfilePersistenceMapper::toDomain);
    }

    @Override
    public Optional<DriverProfile> findByEmployeeCode(String employeeCode, String merchantId) {
        return driverProfileEntityRepository.findByEmployeeCodeAndMerchantId(employeeCode, merchantId)
                .map(DriverProfilePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUserId(String userId, String merchantId) {
        return driverProfileEntityRepository.existsByUserIdAndMerchantId(userId, merchantId);
    }

    @Override
    public boolean existsByEmployeeCode(String employeeCode, String merchantId) {
        return driverProfileEntityRepository.existsByEmployeeCodeAndMerchantId(employeeCode, merchantId);
    }

    @Override
    public List<DriverProfile> findByMerchantId(String merchantId) {
        return driverProfileEntityRepository.findByMerchantId(merchantId).stream()
                .map(DriverProfilePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public PagedResult<DriverProfile> fetch(String merchantId, int pageNumber, int pageSize) {
        Page<DriverProfileEntity> page = driverProfileEntityRepository.findByMerchantId(merchantId, PageRequest.of(pageNumber, pageSize));
        List<DriverProfile> items = page.getContent().stream()
                .map(DriverProfilePersistenceMapper::toDomain)
                .toList();

        return PagedResult.<DriverProfile>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public DriverProfile save(DriverProfile profile) {
        return DriverProfilePersistenceMapper.toDomain(driverProfileEntityRepository.save(DriverProfilePersistenceMapper.toEntity(profile)));
    }
}
