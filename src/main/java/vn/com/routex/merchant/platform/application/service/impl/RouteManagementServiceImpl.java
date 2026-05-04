package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteResult;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchDetailRouteQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchDetailRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesResult;
import vn.com.routex.merchant.platform.application.command.route.RoutePointCommand;
import vn.com.routex.merchant.platform.application.command.route.RoutePointResult;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteResult;
import vn.com.routex.merchant.platform.application.service.RouteManagementService;
import vn.com.routex.merchant.platform.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.merchant.platform.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.operationpoint.port.OperationPointRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.domain.route.model.ProvincesInformationPair;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteProvincesLookupPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.domain.trip.port.TripQueryPort;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleProfile;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_STOP_ORDER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.OPERATION_POINT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_POINT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.STOP_COORDINATES_MUST_BE_PROVIDED_TOGETHER;

@Service
@RequiredArgsConstructor
public class RouteManagementServiceImpl implements RouteManagementService {

    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final RouteStopRepositoryPort routeStopRepositoryPort;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;
    private final RouteProvincesLookupPort routeProvincesLookupPort;
    private final TripQueryPort tripQueryPort;
    private final OperationPointRepositoryPort operationPointRepositoryPort;
    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public CreateRouteResult createRoute(CreateRouteCommand command) {
        String originName = command.originName().trim();
        String destinationName = command.destinationName().trim();

        ProvincesInformationPair codeResult = routeProvincesLookupPort.getCodes(command.originName(), command.destinationName());

        String originCode = codeResult.originCode();
        String destinationCode = codeResult.destinationCode();

        List<RoutePointCommand> routePoints =
                Optional.ofNullable(command.routePoints())
                        .orElseGet(List::of);

        // Validate stop points
        validateRoutePoints(command);

        OffsetDateTime now = OffsetDateTime.now();
        String routeId = UUID.randomUUID().toString();
        List<RouteStopPlan> routeStopPlans = routePoints.stream()
                .map(point -> RouteStopPlan.builder()
                        .id(UUID.randomUUID().toString())
                        .routeId(routeId)
                        .stopOrder(Integer.parseInt(point.operationOrder()))
                        .creator(command.creator())
                        .createdAt(now)
                        .createdBy(command.creator())
                        .note(point.note())
                        .operationPointId(point.operationPointId())
                        .stopName(point.stopName())
                        .stopAddress(point.stopAddress())
                        .stopCity(point.stopCity())
                        .stopLatitude(point.stopLatitude())
                        .stopLongitude(point.stopLongitude())
                        .build())
                .collect(Collectors.toList());

        RouteAggregate newRoute = RouteAggregate.plan(
                routeId,
                command.creator(),
                command.merchantId(),
                originCode,
                destinationCode,
                originName,
                destinationName,
                now,
                routeStopPlans
        );

        routeAggregateRepositoryPort.save(newRoute);
        routeStopRepositoryPort.saveAll(routeStopPlans);


        return CreateRouteResult.builder()
                .id(newRoute.getId())
                .creator(command.creator())
                .originCode(originCode)
                .originName(command.originName())
                .destinationCode(destinationCode)
                .status(RouteStatus.ACTIVE)
                .routePoints(command.routePoints() != null ?
                        command.routePoints() : null)
                .build();
    }

    @Override
    @Transactional
    public UpdateRouteResult updateRoute(UpdateRouteCommand command) {
        RouteAggregate route = routeAggregateRepositoryPort.findById(command.routeId(), command.context().merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, command.routeId()))));

        if (command.routePoints() != null) {
            command.routePoints().forEach(point -> {
                RouteStopPlan routeStopPlan = routeStopRepositoryPort.findByRouteIdAndStopOrder(command.routeId(), point.operationOrder())
                        .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, ROUTE_POINT_NOT_FOUND)));

                Optional.ofNullable(point.note())
                        .ifPresent(routeStopPlan::setNote);

                routeStopRepositoryPort.save(routeStopPlan);
            });
        }


        Optional<ProvincesInformationPair> codeResult = Optional.ofNullable(command.originName())
                        .flatMap(origin -> Optional.ofNullable(command.destinationName())
                                .map(dest -> routeProvincesLookupPort.getCodes(origin, dest)));

        String originCode = codeResult.map(ProvincesInformationPair::originCode).orElse(null);
        String destinationCode = codeResult.map(ProvincesInformationPair::destinationCode).orElse(null);


        Optional.ofNullable(originCode)
                .ifPresent(route::setOriginCode);

        Optional.ofNullable(destinationCode)
                .ifPresent(route::setDestinationCode);

        Optional.ofNullable(command.originName())
                .ifPresent(route::setOriginName);

        Optional.ofNullable(command.destinationName())
                .ifPresent(route::setDestinationName);

        routeAggregateRepositoryPort.save(route);

        List<UpdateRouteResult.UpdateRoutePointResult> routePointResults = command.routePoints() != null ?
                command.routePoints().stream()
                        .map(point -> UpdateRouteResult.UpdateRoutePointResult.builder()
                                .id(point.id())
                                .operationOrder(point.id())
                                .note(point.note())
                                .build())
                        .toList() : null;

        return UpdateRouteResult.builder()
                .routeId(command.routeId())
                .creator(command.creator())
                .originCode(originCode)
                .originName(command.originName())
                .destinationCode(destinationCode)
                .destinationName(command.destinationName())
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

        return DeleteRouteResult.builder()
                .creator(command.creator())
                .routeId(route.getId())
                .status(route.getStatus().name())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    @Override
    public FetchRoutesResult fetchRoutes(FetchRoutesQuery query) {

        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        validatePaging(query, pageSize, pageNumber);
        PagedResult<RouteAggregate> page = routeAggregateRepositoryPort.fetch(query.context().merchantId(), pageNumber - 1, pageSize);
        return FetchRoutesResult.builder()
                .items(page.getItems().stream()
                        .map(this::toFetchDetailResult)
                        .toList())
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public FetchDetailRouteResult fetchDetailRoute(FetchDetailRouteQuery query) {

        sLog.info("[ROUTE-DETAIL] Fetch Detail Query: {}", query);

        RouteAggregate routeAggregate = routeAggregateRepositoryPort.findById(query.routeId(), query.merchantId())
                .orElseThrow(() -> new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, query.routeId()))));

        TripAssignmentRecord tripAssignmentRecord = tripAssignmentRepositoryPort.findByTripIdAndMerchantId(query.routeId(), query.merchantId())
                .orElse(null);

        VehicleProfile vehicleProfile = null;
        VehicleTemplate vehicleTemplate = null;

        if(tripAssignmentRecord != null) {
            vehicleProfile = vehicleProfileRepositoryPort.findById(tripAssignmentRecord.getVehicleId()).orElse(null);
            vehicleTemplate = vehicleTemplateRepositoryPort.findById(vehicleProfile.getTemplateId()).orElse(null);
        }

        List<RouteStopPlan> routeStopPlans = routeStopRepositoryPort.findByRouteId(query.routeId());

        List<RoutePointResult> routePointResults = routeStopPlans.isEmpty() ? null :
                routeStopPlans.stream()
                        .map(stop -> RoutePointResult.builder()
                                .id(stop.getId())
                                .routeId(stop.getRouteId())
                                .operationOrder(stop.getStopOrder())
                                .operationPointId(stop.getOperationPointId())
                                .stopAddress(stop.getStopAddress())
                                .stopName(stop.getStopName())
                                .stopCity(stop.getStopCity())
                                .stopLatitude(stop.getStopLatitude())
                                .stopLongitude(stop.getStopLongitude())
                                .note(stop.getNote())
                                .build())
                        .toList();

        return FetchDetailRouteResult.builder()
                .id(routeAggregate.getId())
                .creator(routeAggregate.getCreator())
                .originCode(routeAggregate.getOriginCode())
                .originName(routeAggregate.getOriginName())
                .destinationCode(routeAggregate.getDestinationCode())
                .destinationName(routeAggregate.getDestinationName())
                .status(routeAggregate.getStatus())
                .availableSeats(vehicleTemplate != null ? vehicleTemplate.getSeatCapacity() : null)
                .vehicleId(tripAssignmentRecord != null ? tripAssignmentRecord.getVehicleId() : null)
                .vehiclePlate(vehicleProfile != null ? vehicleProfile.getVehiclePlate() : null)
                .hasFloor(vehicleProfile != null ? vehicleProfile.isHasFloor() : null)
                .assignedAt(tripAssignmentRecord != null ? tripAssignmentRecord.getAssignedAt() : null)
                .routePoints(routePointResults)
                .build();
    }

    private FetchRouteResult toFetchDetailResult(RouteAggregate aggregate) {

        return FetchRouteResult.builder()
                .id(aggregate.getId())
                .creator(aggregate.getCreator())
                .originCode(aggregate.getOriginCode())
                .originName(aggregate.getOriginName())
                .destinationCode(aggregate.getDestinationCode())
                .destinationName(aggregate.getDestinationName())
                .status(aggregate.getStatus())
                .routePoints(
                        aggregate.getStopPlans().stream()
                                .map(stop -> RoutePointResult.builder()
                                        .id(stop.getId())
                                        .operationOrder(stop.getStopOrder())
                                        .routeId(stop.getRouteId())
                                        .note(stop.getNote())
                                        .operationPointId(stop.getOperationPointId())
                                        .stopName(stop.getStopName())
                                        .stopAddress(stop.getStopAddress())
                                        .stopCity(stop.getStopCity())
                                        .stopLatitude(stop.getStopLatitude())
                                        .stopLongitude(stop.getStopLongitude())
                                        .build()
                                )
                                .toList())
                .build();
    }
    private void validatePaging(FetchRoutesQuery query, int pageSize, int pageNumber) {
        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }
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
