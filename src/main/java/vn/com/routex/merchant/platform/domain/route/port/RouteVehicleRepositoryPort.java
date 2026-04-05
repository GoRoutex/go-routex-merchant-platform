package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.domain.route.model.VehicleSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteVehicleRepositoryPort {
    Optional<VehicleSnapshot> findById(String vehicleId);

    Map<String, VehicleSnapshot> findByIds(List<String> vehicleIds);
}
