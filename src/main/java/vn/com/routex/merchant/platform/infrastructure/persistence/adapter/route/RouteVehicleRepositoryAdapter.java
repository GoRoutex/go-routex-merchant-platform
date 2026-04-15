package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.route.model.VehicleSnapshot;
import vn.com.routex.merchant.platform.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.vehicle.repository.VehicleEntityRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteVehicleRepositoryAdapter implements RouteVehicleRepositoryPort {

    private final VehicleEntityRepository vehicleEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId) {
        return vehicleEntityRepository.findById(vehicleId)
                .map(routePersistenceMapper::toVehicleSnapshot);
    }

    @Override
    public Optional<VehicleSnapshot> findById(String vehicleId, String merchantId) {
        return vehicleEntityRepository.findByIdAndMerchantId(vehicleId, merchantId)
                .map(routePersistenceMapper::toVehicleSnapshot);
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds) {
        return vehicleEntityRepository.findByIdIn(vehicleIds).stream()
                .map(routePersistenceMapper::toVehicleSnapshot)
                .collect(Collectors.toMap(VehicleSnapshot::getId, vehicle -> vehicle));
    }

    @Override
    public Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds, String merchantId) {
        return vehicleEntityRepository.findByIdInAndMerchantId(vehicleIds, merchantId).stream()
                .map(routePersistenceMapper::toVehicleSnapshot)
                .collect(Collectors.toMap(VehicleSnapshot::getId, vehicle -> vehicle));
    }
}
