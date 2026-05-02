package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;

import java.util.List;
import java.util.Optional;

public interface RouteAggregateRepositoryPort {
    Optional<RouteAggregate> findById(String routeId);

    Optional<RouteAggregate> findById(String routeId, String merchantId);

    List<RouteAggregate> findByMerchantId(String merchantId);

    void save(RouteAggregate aggregate);
}
