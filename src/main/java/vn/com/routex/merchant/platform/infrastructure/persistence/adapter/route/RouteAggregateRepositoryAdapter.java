package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteAggregateRepositoryAdapter implements RouteAggregateRepositoryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;
    private final RouteStopRepositoryPort routeStopRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

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
    public PagedResult<RouteAggregate> fetch(String merchantId, int pageNumber, int pageSize) {
        Page<RouteEntity> page = routeEntityRepository.findByMerchantId(merchantId, PageRequest.of(pageNumber, pageSize));
        return PagedResult.<RouteAggregate>builder()
                .items(page.getContent().stream()
                        .map(p -> {
                            RouteAggregate routeAggregate = routePersistenceMapper.toAggregate(p);
                            List<RouteStopPlan> stopPlans = routeStopRepositoryPort.findByRouteId(routeAggregate.getId());
                            routeAggregate.setStopPlans(stopPlans);
                            return routeAggregate;
                        })
                        .toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
