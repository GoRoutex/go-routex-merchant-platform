package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public Optional<RouteAggregate> findById(String routeId) {
        return routeEntityRepository.findById(routeId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public Optional<RouteAggregate> findById(String routeId, String merchantId) {
        return routeEntityRepository.findByIdAndMerchantId(routeId, merchantId)
                .map(routePersistenceMapper::toAggregate);
    }

    @Override
    public List<RouteAggregate> findByMerchantId(String merchantId) {
        return routeEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public void save(RouteAggregate aggregate) {
        routeEntityRepository.save(routePersistenceMapper.toEntity(aggregate));
    }

    @Override
    public String generateRouteCode(String originCode, String destinationCode) {
        return routeEntityRepository.generateRouteCode(originCode, destinationCode);
    }
}
