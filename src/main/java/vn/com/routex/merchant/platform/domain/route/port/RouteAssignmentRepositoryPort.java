package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.domain.route.model.RouteAssignmentRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RouteAssignmentRepositoryPort {
    boolean existsActiveByRouteId(String routeId);

    List<RouteAssignmentRecord> findByMerchantId(String merchantId);

    Optional<RouteAssignmentRecord> findActiveByRouteId(String routeId);

    Map<String, RouteAssignmentRecord> findLatestActiveByRouteIds(List<String> routeIds);

    void save(RouteAssignmentRecord assignment);
}
