package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.maintenance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.domain.maintenance.model.MaintenancePlan;
import vn.com.routex.merchant.platform.domain.maintenance.port.MaintenancePlanRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.maintenance.MaintenancePlanEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.maintenance.repository.MaintenancePlanEntityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MaintenancePlanRepositoryAdapter implements MaintenancePlanRepositoryPort {

    private final MaintenancePlanEntityRepository maintenancePlanEntityRepository;

    @Override
    public Optional<MaintenancePlan> findById(String id) {
        return maintenancePlanEntityRepository.findById(id)
                .map(MaintenancePlanPersistenceMapper::toDomain);
    }

    @Override
    public Optional<MaintenancePlan> findById(String id, String merchantId) {
        return maintenancePlanEntityRepository.findByIdAndMerchantId(id, merchantId)
                .map(MaintenancePlanPersistenceMapper::toDomain);
    }

    @Override
    public List<MaintenancePlan> findByVehicleId(String vehicleId, String merchantId) {
        return maintenancePlanEntityRepository.findByVehicleIdAndMerchantId(vehicleId, merchantId).stream()
                .map(MaintenancePlanPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCode(String code, String merchantId) {
        return maintenancePlanEntityRepository.existsByCodeAndMerchantId(code, merchantId);
    }

    @Override
    public void save(MaintenancePlan maintenancePlan) {
        maintenancePlanEntityRepository.save(MaintenancePlanPersistenceMapper.toEntity(maintenancePlan));
    }

    @Override
    public PagedResult<MaintenancePlan> fetch(
            String merchantId,
            String vehicleId,
            MaintenancePlanStatus status,
            MaintenancePlanType type,
            LocalDate fromPlannedDate,
            LocalDate toPlannedDate,
            int pageNumber,
            int pageSize
    ) {
        Page<MaintenancePlanEntity> page = maintenancePlanEntityRepository.findByFilters(
                merchantId,
                vehicleId,
                status,
                type,
                fromPlannedDate,
                toPlannedDate,
                PageRequest.of(pageNumber, pageSize));
        List<MaintenancePlan> items = page.getContent().stream()
                .map(MaintenancePlanPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<MaintenancePlan>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
