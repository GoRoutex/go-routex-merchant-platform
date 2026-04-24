package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.route.RoutePointResult;
import vn.com.routex.merchant.platform.application.specification.RouteSpecification;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.route.model.VehicleSnapshot;
import vn.com.routex.merchant.platform.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteQueryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.readmodel.RouteFetchView;
import vn.com.routex.merchant.platform.domain.route.readmodel.RouteSearchView;
import vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteQueryAdapter implements RouteQueryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RouteStopRepositoryPort routeStopRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    public List<RouteSearchView> searchAssignedRoutes(
            String merchantId,
            String origin,
            String destination,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            int pageNumber,
            int pageSize
    ) {
        Specification<RouteEntity> specification = Specification.where(RouteSpecification.hasMerchantId(merchantId))
                .and(RouteSpecification.originContainsIgnoreCase(origin))
                .and(RouteSpecification.destinationContainsIgnoreCase(destination))
                .and(RouteSpecification.plannedStartBetween(startTime, endTime))
                .and(RouteSpecification.assignedStatus(RouteStatus.ASSIGNED));

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "plannedStartTime"));

        return routeEntityRepository.findAll(specification, pageable)
                .getContent()
                .stream()
                .map(route -> {
                    RouteSearchView searchView = new RouteSearchView();
                    searchView.setId(route.getId());
                    searchView.setRouteCode(route.getRouteCode());
                    searchView.setPickupBranch(route.getPickupBranch());
                    searchView.setOrigin(route.getOrigin());
                    searchView.setDestination(route.getDestination());
                    searchView.setPlannedStartTime(toDefaultOffset(route.getPlannedStartTime()));
                    searchView.setPlannedEndTime(toDefaultOffset(route.getPlannedEndTime()));
                    return searchView;
                })
                .toList();
    }

    @Override
    public PagedResult<RouteFetchView> fetchRoutes(String merchantId, String merchantName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "plannedStartTime"));
        Specification<RouteEntity> specification = Specification.where(RouteSpecification.hasMerchantId(merchantId))
                .and(RouteSpecification.hasMerchantName(merchantName));
        Page<RouteEntity> page = routeEntityRepository.findAll(specification, pageable);

        sLog.info("PAGE INFO: {}", page);
        List<RouteFetchView> items = page.getContent().stream()
                .map(route -> {

                    List<RouteStopPlan> routeStopPlans = routeStopRepositoryPort.findByRouteId(route.getId());
                    RouteAssignmentRecord assignmentRecord = routeAssignmentRepositoryPort.findByRouteIdAndMerchantId(route.getId(), route.getMerchantId())
                            .orElse(null);

                    VehicleSnapshot snapshot = null;
                    if(assignmentRecord != null) {
                        snapshot = routeVehicleRepositoryPort.findById(assignmentRecord.getVehicleId()).orElse(null);
                    }

                    RouteFetchView view = new RouteFetchView();
                    view.setId(route.getId());
                    view.setRouteCode(route.getRouteCode());
                    view.setCreator(route.getCreator());
                    view.setPickupBranch(route.getPickupBranch());
                    view.setOrigin(route.getOrigin());
                    view.setDestination(route.getDestination());
                    view.setPlannedStartTime(toDefaultOffset(route.getPlannedStartTime()));
                    view.setPlannedEndTime(toDefaultOffset(route.getPlannedEndTime()));
                    view.setActualStartTime(toDefaultOffset(route.getActualStartTime()));
                    view.setActualEndTime(toDefaultOffset(route.getActualEndTime()));
                    view.setStatus(route.getStatus() == null ? null : route.getStatus().name());
                    view.setRoutePoints(routeStopPlans.stream().map(
                            stop -> RoutePointResult.builder()
                                    .id(stop.getId())
                                    .operationOrder(String.valueOf(stop.getStopOrder()))
                                    .routeId(stop.getRouteId())
                                    .plannedArrivalTime(toDefaultOffset(stop.getPlannedArrivalTime()))
                                    .plannedDepartureTime(toDefaultOffset(stop.getPlannedDepartureTime()))
                                    .note(stop.getNote())
                                    .operationPointId(stop.getOperationPointId())
                                    .stopName(stop.getStopName())
                                    .stopAddress(stop.getStopAddress())
                                    .stopCity(stop.getStopCity())
                                    .stopLatitude(stop.getStopLatitude())
                                    .stopLongitude(stop.getStopLongitude())
                                    .build())
                            .toList()
                    );
                    view.setAssignedAt(assignmentRecord != null ? toDefaultOffset(assignmentRecord.getAssignedAt()) : null);
                    view.setVehicleId(assignmentRecord != null ? assignmentRecord.getVehicleId() : null);
                    view.setVehiclePlate(snapshot != null ? snapshot.getVehiclePlate() : null);
                    return view;
                })
                .toList();

        return PagedResult.<RouteFetchView>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private OffsetDateTime toDefaultOffset(OffsetDateTime dateTime) {
        return dateTime == null
                ? null
                : dateTime.atZoneSameInstant(ApplicationConstant.DEFAULT_ZONE).toOffsetDateTime();
    }
}
