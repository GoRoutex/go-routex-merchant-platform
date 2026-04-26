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
import vn.com.routex.merchant.platform.domain.driver.model.DriverProfile;
import vn.com.routex.merchant.platform.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteQueryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.readmodel.RouteFetchView;
import vn.com.routex.merchant.platform.domain.route.readmodel.RouteSearchView;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleProfile;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.repository.RouteEntityRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteQueryAdapter implements RouteQueryPort {

    private final RouteEntityRepository routeEntityRepository;
    private final RouteStopRepositoryPort routeStopRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final DriverProfileRepositoryPort driverProfileRepositoryPort;

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
        Specification<RouteEntity> specification = Specification
                .where(RouteSpecification.hasMerchantId(merchantId))
                .and(RouteSpecification.hasMerchantName(merchantName));
        Page<RouteEntity> page = routeEntityRepository.findAll(specification, pageable);

        // refactor from n+1 issues to batch loading

        List<RouteEntity> routes = page.getContent();

        if (routes.isEmpty()) {
            return PagedResult.<RouteFetchView>builder()
                    .items(List.of())
                    .pageNumber(page.getNumber())
                    .pageSize(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .build();
        }


        List<String> routeIds = routes.stream()
                .map(RouteEntity::getId)
                .toList();

        Map<String, List<RouteStopPlan>> routeStopsPlan = routeStopRepositoryPort.findByRouteIds(routeIds);


        List<RouteAssignmentRecord> assignmentRecords = routeAssignmentRepositoryPort.findByRouteIdAndMerchantId(routeIds, merchantId);

        Map<String, RouteAssignmentRecord> assignmentByRouteId = assignmentRecords.stream()
                .collect(Collectors.toMap(
                        RouteAssignmentRecord::getRouteId,
                        Function.identity(),
                                (oldValue, newValue) -> newValue
                        ));

        Set<String> vehicleIds = assignmentRecords.stream()
                .map(RouteAssignmentRecord::getVehicleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> driverIds = assignmentRecords.stream()
                .map(RouteAssignmentRecord::getDriverId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 5. Batch load vehicles
        Map<String, VehicleProfile> vehicleById = vehicleProfileRepositoryPort
                .findByIdIn(vehicleIds)
                .stream()
                .collect(Collectors.toMap(
                        VehicleProfile::getId,
                        Function.identity(),
                        (oldValue, newValue) -> newValue
                ));

        Map<String, DriverProfile> driverById = driverProfileRepositoryPort
                .findByIdIn(driverIds)
                .stream()
                .collect(Collectors.toMap(
                        DriverProfile::getId,
                        Function.identity(),
                        (oldValue, newValue) -> newValue
                ));

        List<RouteFetchView> items = toRouteFetchViewList(routes, vehicleById, driverById, routeStopsPlan, assignmentByRouteId);
        return PagedResult.<RouteFetchView>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private List<RouteFetchView> toRouteFetchViewList(List<RouteEntity> routes,
                                                      Map<String, VehicleProfile> vehicleIds,
                                                      Map<String, DriverProfile> driverIds,
                                                      Map<String, List<RouteStopPlan>> routeStopPlan,
                                                      Map<String, RouteAssignmentRecord> routeAssignment) {
        return routes.stream()
                .map(route -> {
                    List<RouteStopPlan> routeStopPlans = routeStopPlan.getOrDefault(route.getId(), List.of());

                    RouteAssignmentRecord assignmentRecord = routeAssignment.get(route.getId());
                    VehicleProfile vehicleProfile = null;
                    DriverProfile driverProfile = null;

                    if(assignmentRecord != null) {
                        vehicleProfile = vehicleIds.get(assignmentRecord.getVehicleId());
                        driverProfile = driverIds.get(assignmentRecord.getDriverId());
                    }
                    return RouteFetchView.builder()
                            .id(route.getId())
                            .routeCode(route.getRouteCode())
                            .creator(route.getCreator())
                            .pickupBranch(route.getPickupBranch())
                            .origin(route.getOrigin())
                            .destination(route.getDestination())
                            .plannedStartTime(route.getPlannedStartTime())
                            .plannedEndTime(route.getPlannedEndTime())
                            .actualStartTime(route.getActualStartTime())
                            .actualEndTime(route.getActualEndTime())
                            .status(route.getStatus() != null ? route.getStatus().name() : null)
                            .assignmentResult(RouteFetchView.AssignmentResult.builder()
                                    .vehicleId(assignmentRecord != null ? assignmentRecord.getVehicleId() : null)
                                    .vehiclePlate(vehicleProfile != null ? vehicleProfile.getVehiclePlate() : null)
                                    .driverId(assignmentRecord != null ? assignmentRecord.getDriverId() : null)
                                    .driverName(driverProfile != null ? driverProfile.getFullName() : null)
                                    .build())
                            .routePoints(routeStopPlans.stream()
                                    .map(stop -> RoutePointResult.builder()
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
                                    .toList())
                            .assignedAt(assignmentRecord != null
                                    ? toDefaultOffset(assignmentRecord.getAssignedAt())
                                    : null)
                            .build();
                })
                .collect(Collectors.toList());
    }
    private OffsetDateTime toDefaultOffset(OffsetDateTime dateTime) {
        return dateTime == null
                ? null
                : dateTime.atZoneSameInstant(ApplicationConstant.DEFAULT_ZONE).toOffsetDateTime();
    }
}
