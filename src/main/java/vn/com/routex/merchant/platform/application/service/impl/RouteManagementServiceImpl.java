package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteResult;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteResult;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesResult;
import vn.com.routex.merchant.platform.application.command.route.RoutePointCommand;
import vn.com.routex.merchant.platform.application.command.route.RoutePointResult;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteResult;
import vn.com.routex.merchant.platform.application.service.OutBoxService;
import vn.com.routex.merchant.platform.application.service.RouteManagementService;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.operationpoint.port.OperationPointRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.domain.route.model.ProvincesCodePair;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.route.model.VehicleSnapshot;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteQueryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteVehicleRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.readmodel.RouteFetchView;
import vn.com.routex.merchant.platform.infrastructure.kafka.event.RouteAssignedEvent;
import vn.com.routex.merchant.platform.infrastructure.kafka.event.RouteSellableEvent;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ROUTE_ASSIGNMENT;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PLANNED_TIME;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_START_TIME;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_STOP_ORDER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.OPERATION_POINT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_POINT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.STOP_COORDINATES_MUST_BE_PROVIDED_TOGETHER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {



    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteStopRepositoryPort routeStopRepositoryPort;
    private final RouteAssignmentRepositoryPort routeAssignmentRepositoryPort;
    private final RouteVehicleRepositoryPort routeVehicleRepositoryPort;
    private final RouteProvincesLookupPort routeProvincesLookupPort;
    private final RouteQueryPort routeQueryPort;
    private final OperationPointRepositoryPort operationPointRepositoryPort;
    private final OutBoxService outBoxService;

    @Value("${spring.kafka.topics.routes}")
    private String routeTopic;

    @Value("${spring.kafka.events.route-assigned}")
    private String routeAssignedEvent;

    @Value("${spring.kafka.events.route-ready-for-sale}")
    private String routeReadyForSaleEvent;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public CreateRouteResult createRoute(CreateRouteCommand command) {
        String origin = command.origin().trim();
        String destination = command.destination().trim();

        OffsetDateTime plannedStartTime = OffsetDateTime.parse(command.plannedStartTime());
        OffsetDateTime plannedEndTime = OffsetDateTime.parse(command.plannedEndTime());

        ProvincesCodePair codeResult = routeProvincesLookupPort.getCodes(command.origin(), command.destination());

        String originCode = codeResult.originCode();
        String destinationCode = codeResult.destinationCode();

        String routeCode = routeAggregateRepositoryPort.generateRouteCode(originCode, destinationCode);

        if(!plannedStartTime.isBefore(plannedEndTime)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_START_TIME));
        }


        List<RoutePointCommand> routePoints =
                Optional.ofNullable(command.routePoints())
                        .orElseGet(List::of);

        // Validate stop points
        validateRoutePoints(command);

        OffsetDateTime now = OffsetDateTime.now();
        String routeId = UUID.randomUUID().toString();
        List<RouteStopPlan> routeStopPlans = routePoints.stream()
                .map(point -> {
                    OffsetDateTime arrival = OffsetDateTime.parse(point.plannedArrivalTime());
                    OffsetDateTime departure = OffsetDateTime.parse(point.plannedDepartureTime());

                    return RouteStopPlan.builder()
                            .id(UUID.randomUUID().toString())
                            .routeId(routeId)
                            .stopOrder(Integer.parseInt(point.operationOrder()))
                            .creator(command.creator())
                            .createdAt(now)
                            .createdBy(command.creator())
                            .plannedArrivalTime(arrival)
                            .plannedDepartureTime(departure)
                            .note(point.note())
                            .operationPointId(point.operationPointId())
                            .stopName(point.stopName())
                            .stopAddress(point.stopAddress())
                            .stopCity(point.stopCity())
                            .stopLatitude(point.stopLatitude())
                            .stopLongitude(point.stopLongitude())
                            .build();
                })
                .collect(Collectors.toList());

        RouteAggregate newRoute = RouteAggregate.plan(
                routeId,
                routeCode,
                command.creator(),
                command.merchantId(),
                command.pickupBranch(),
                origin,
                destination,
                plannedStartTime,
                plannedEndTime,
                now,
                routeStopPlans
        );

        routeAggregateRepositoryPort.save(newRoute);
        routeStopRepositoryPort.saveAll(routeStopPlans);


        return CreateRouteResult.builder()
                .id(newRoute.getId())
                .routeCode(routeCode)
                .creator(command.creator())
                .pickupBranch(command.pickupBranch())
                .origin(command.origin())
                .destination(command.destination())
                .plannedStartTime(command.plannedStartTime())
                .plannedEndTime(command.plannedEndTime())
                .status(RouteStatus.PLANNED.name())
                .routePoints(command.routePoints() != null ?
                        command.routePoints() : null)
                .build();
    }

    @Override
    @Transactional
    public AssignRouteResult assignRoute(AssignRouteCommand command) {
        if(routeAssignmentRepositoryPort.existsActiveByRouteId(command.routeId(), command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, command.routeId())));
        }

        VehicleSnapshot vehicle = routeVehicleRepositoryPort.findById(command.vehicleId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId(), command.merchantId())
                        .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        OffsetDateTime assignedAt = OffsetDateTime.now();
        RouteAssignmentRecord routeAssignment = RouteAssignmentRecord.assign(
                UUID.randomUUID().toString(),
                route.getMerchantId(),
                command.routeId(),
                command.creator(),
                vehicle.getId(),
                command.driverId(),
                assignedAt
        );

        route.setStatus(RouteStatus.ASSIGNED);
        routeAggregateRepositoryPort.save(route);
        routeAssignmentRepositoryPort.save(routeAssignment);

        sLog.info("[ASSIGN-ROUTE] Route Assigned successfully with VehicleId: {} DriverId: {}", vehicle.getId(), command.driverId());

        RouteSellableEvent sellableEvent = RouteSellableEvent
                .builder()
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .assignedBy(command.creator())
                .assignedAt(routeAssignment.getAssignedAt())
                .routeStatus(RouteStatus.ASSIGNED.name())
                .seatCount(vehicle.getSeatCapacity())
                .hasFloor(vehicle.isHasFloor())
                .creator(command.creator())
                .build();

        outBoxService.generateEvent(routeAssignment.getRouteId(), routeTopic, routeReadyForSaleEvent, routeAssignment.getId(), sellableEvent, ApiRequestUtils.getHeader(command.context()));

        RouteAssignedEvent assignedEvent = RouteAssignedEvent
                .builder()
                .routeId(routeAssignment.getRouteId())
                .driverUserId(routeAssignment.getDriverId()) // TODO: Get User Id from driverId
                .driverId(routeAssignment.getDriverId())
                .vehicleId(routeAssignment.getVehicleId())
                .originName(route.getOrigin())
                .destinationName(route.getDestination())
                .departureTime(route.getPlannedStartTime())
                .status(route.getStatus())
                .assignedBy(command.creator())
                .assignedAt(routeAssignment.getAssignedAt())
                .build();


        outBoxService.generateEvent(routeAssignment.getRouteId(), routeTopic, routeAssignedEvent, routeAssignment.getId(), assignedEvent, ApiRequestUtils.getHeader(command.context()));

        return AssignRouteResult.builder()
                .creator(command.creator())
                .assignedAt(routeAssignment.getAssignedAt().toString())
                .routeId(routeAssignment.getRouteId())
                .vehicleId(routeAssignment.getVehicleId())
                .driverId(routeAssignment.getDriverId())
                .status(routeAssignment.getStatus().name())
                .build();
    }

    @Override
    @Transactional
    public UpdateRouteResult updateRoute(UpdateRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId(), command.context().merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        if(command.routePoints() != null) {
            command.routePoints().forEach(point -> {
                RouteStopPlan routeStopPlan = routeStopRepositoryPort.findByRouteIdAndStopOrder(command.routeId(), point.operationOrder())
                        .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_POINT_NOT_FOUND)));

                Optional.ofNullable(point.plannedArrivalTime())
                        .ifPresent(routeStopPlan::setPlannedArrivalTime);

                Optional.ofNullable(point.plannedDepartureTime())
                        .ifPresent(routeStopPlan::setPlannedDepartureTime);

                Optional.ofNullable(point.note())
                        .ifPresent(routeStopPlan::setNote);

                routeStopRepositoryPort.save(routeStopPlan);
            });
        }
        Optional.ofNullable(command.pickupBranch())
                .ifPresent(route::setPickupBranch);

        Optional.ofNullable(command.origin())
                .ifPresent(route::setOrigin);

        Optional.ofNullable(command.destination())
                .ifPresent(route::setDestination);

        Optional.ofNullable(command.plannedStartTime())
                .ifPresent(route::setPlannedStartTime);

        Optional.ofNullable(command.plannedEndTime())
                .ifPresent(route::setPlannedEndTime);

        Optional.ofNullable(command.actualStartTime())
                .ifPresent(route::setActualStartTime);

        Optional.ofNullable(command.actualEndTime())
                .ifPresent(route::setActualEndTime);

        routeAggregateRepositoryPort.save(route);

        List<UpdateRouteResult.UpdateRoutePointResult> routePointResults = command.routePoints() != null ?
                command.routePoints().stream()
                .map(point -> UpdateRouteResult.UpdateRoutePointResult.builder()
                        .id(point.id())
                        .operationOrder(point.id())
                        .plannedArrivalTime(point.plannedArrivalTime())
                        .plannedDepartureTime(point.plannedDepartureTime())
                        .note(point.note())
                        .build())
                .toList() : null;

        return UpdateRouteResult.builder()
                .routeId(command.routeId())
                .creator(command.creator())
                .pickupBranch(command.pickupBranch())
                .origin(command.origin())
                .destination(command.destination())
                .plannedStartTime(command.plannedStartTime())
                .plannedEndTime(command.plannedEndTime())
                .actualStartTime(command.actualStartTime())
                .actualEndTime(command.actualEndTime())
                .status(command.status())
                .routePoints(routePointResults)
                .build();
    }
    @Override
    @Transactional
    public DeleteRouteResult deleteRoute(DeleteRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));


        OffsetDateTime now = OffsetDateTime.now();
        route.cancel(command.creator(), now);
        routeAggregateRepositoryPort.save(route);

        routeAssignmentRepositoryPort
                .findActiveByRouteId(route.getId(), command.merchantId())
                .ifPresent(routeAssignment -> {
                    routeAssignment.cancel(command.creator(), now);
                    routeAssignmentRepositoryPort.save(routeAssignment);
                });

        return DeleteRouteResult.builder()
                .creator(command.creator())
                .routeId(route.getId())
                .routeCode(route.getRouteCode())
                .status(route.getStatus().name())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    @Override
    public FetchRoutesResult fetchRoutes(FetchRoutesQuery query) {
        int pageNumber = ApiRequestUtils.parseIntOrDefault(
                query.pageNumber(),
                1,
                "pageNumber",
                query.context().requestId(),
                query.context().requestDateTime(),
                query.context().channel()
        );
        int pageSize = ApiRequestUtils.parseIntOrDefault(
                query.pageSize(),
                10,
                "pageSize",
                query.context().requestId(),
                query.context().requestDateTime(),
                query.context().channel()
        );

        PagedResult<RouteFetchView> page = routeQueryPort.fetchRoutes(
                query.merchantId(),
                query.merchantName(),
                Math.max(pageNumber - 1, 0),
                pageSize
        );

        return FetchRoutesResult.builder()
                .items(page.getItems().stream()
                        .map(this::toFetchRouteResult)
                        .toList())
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private RoutePointResult toRoutePoint(RouteStopPlan s) {
        return RoutePointResult.builder()
                .id(s.getId())
                .operationOrder(String.valueOf(s.getStopOrder()))
                .routeId(s.getRouteId())
                .plannedArrivalTime(s.getPlannedArrivalTime() == null ? null : s.getPlannedArrivalTime())
                .plannedDepartureTime(s.getPlannedDepartureTime() == null ? null : s.getPlannedDepartureTime())
                .note(s.getNote())
                .operationPointId(s.getOperationPointId())
                .stopName(s.getStopName())
                .stopAddress(s.getStopAddress())
                .stopCity(s.getStopCity())
                .stopLatitude(s.getStopLatitude())
                .stopLongitude(s.getStopLongitude())
                .build();
    }

    private FetchRouteResult toFetchRouteResult(RouteFetchView view) {
        return FetchRouteResult.builder()
                .id(view.getId())
                .creator(view.getCreator())
                .pickupBranch(view.getPickupBranch())
                .routeCode(view.getRouteCode())
                .origin(view.getOrigin())
                .destination(view.getDestination())
                .plannedStartTime(view.getPlannedStartTime())
                .plannedEndTime(view.getPlannedEndTime())
                .actualStartTime(view.getActualStartTime())
                .actualEndTime(view.getActualEndTime())
                .status(view.getStatus())
                .availableSeats(view.getAvailableSeats())
                .vehicleId(view.getVehicleId())
                .vehiclePlate(view.getVehiclePlate())
                .hasFloor(view.getHasFloor())
                .assignedAt(view.getAssignedAt())
                .routePoints(view.getRoutePoints())
                .build();
    }

    private void validateRoutePoints(CreateRouteCommand command) {
        List<RoutePointCommand> routePoints = command.routePoints();
        if (routePoints == null || routePoints.isEmpty()) {
            return;
        }

        Set<Integer> setOfOrders = new HashSet<>();

        for (RoutePointCommand point : routePoints) {
            validateRoutePoint(command, point, setOfOrders);
        }
    }

    private void validateRoutePoint(CreateRouteCommand command, RoutePointCommand point, Set<Integer> setOfOrders) {
        Integer operationOrder = validateStopOrder(command, point);
        if (!setOfOrders.add(operationOrder)) {
            throwInvalidInput(command, INVALID_STOP_ORDER);
        }

        validatePlannedTime(command, point);

        boolean hasOperationPointId = hasText(point.operationPointId());

        if (hasOperationPointId) {
            validateOperationPoint(command, point.operationPointId().trim());
            return;
        }

        validateCustomStopCoordinates(command, point);
    }

    private Integer validateStopOrder(CreateRouteCommand command, RoutePointCommand point) {
        if (point.operationOrder() == null) {
            throwInvalidInput(command, INVALID_STOP_ORDER);
        }

        try {
            int operationOrder = Integer.parseInt(point.operationOrder());
            if (operationOrder <= 0) {
                throwInvalidInput(command, INVALID_STOP_ORDER);
            }
            return operationOrder;
        } catch (NumberFormatException exception) {
            throwInvalidInput(command, INVALID_STOP_ORDER);
            return null;
        }
    }

    private void validatePlannedTime(CreateRouteCommand command, RoutePointCommand point) {
        if (point.plannedArrivalTime() == null || point.plannedDepartureTime() == null) {
            throwInvalidInput(command, INVALID_PLANNED_TIME);
        }

        try {
            OffsetDateTime plannedArrivalTime = OffsetDateTime.parse(point.plannedArrivalTime());
            OffsetDateTime plannedDepartureTime = OffsetDateTime.parse(point.plannedDepartureTime());

            if (!plannedArrivalTime.isBefore(plannedDepartureTime)) {
                throwInvalidInput(command, INVALID_PLANNED_TIME);
            }
        } catch (DateTimeParseException exception) {
            throwInvalidInput(command, INVALID_PLANNED_TIME);
        }
    }

    private void validateOperationPoint(CreateRouteCommand command, String operationPointId) {
        operationPointRepositoryPort.findById(operationPointId, command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(
                                RECORD_NOT_FOUND,
                                String.format(OPERATION_POINT_NOT_FOUND, operationPointId))));
    }

    private void validateCustomStopCoordinates(CreateRouteCommand command, RoutePointCommand point) {
        if (point.stopLatitude() != null ^ point.stopLongitude() != null) {
            throwInvalidInput(command, STOP_COORDINATES_MUST_BE_PROVIDED_TOGETHER);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void throwInvalidInput(CreateRouteCommand command, String message) {
        throw new BusinessException(
                command.context().requestId(),
                command.context().requestDateTime(),
                command.context().channel(),
                ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, message));
    }
}
