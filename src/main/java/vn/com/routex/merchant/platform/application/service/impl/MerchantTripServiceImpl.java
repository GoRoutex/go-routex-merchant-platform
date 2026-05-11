package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteResult;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripResult;
import vn.com.routex.merchant.platform.application.command.trip.DeleteTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.DeleteTripResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListResult;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripResult;
import vn.com.routex.merchant.platform.application.service.MerchantTripService;
import vn.com.routex.merchant.platform.application.service.OutBoxService;
import vn.com.routex.merchant.platform.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.merchant.platform.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.domain.trip.model.TripAggregate;
import vn.com.routex.merchant.platform.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleProfile;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.kafka.event.TripAssignedEvent;
import vn.com.routex.merchant.platform.infrastructure.kafka.event.TripSellableEvent;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ROUTE_ASSIGNMENT;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.TRIP_ALREADY_EXISTS_FOR_ROUTE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.TRIP_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.VEHICLE_TEMPLATE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantTripServiceImpl implements MerchantTripService {

    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;
    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;
    private final OutBoxService outBoxService;

    @Value("${spring.kafka.topics.trips}")
    private String tripTopic;

    @Value("${spring.kafka.events.trip-assigned}")
    private String tripAssignedEvent;

    @Value("${spring.kafka.events.trip-ready-for-sale}")
    private String tripReadyForSaleEvent;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public CreateTripResult createTrip(CreateTripCommand command) {
        RouteAggregate route = findRoute(command.routeId(), command.merchantId(), command.context().requestId(),
                command.context().requestDateTime(), command.context().channel());

        String tripCode = tripAggregateRepositoryPort.generateTripCode(route.getOriginCode(), route.getDestinationCode());
        OffsetDateTime now = OffsetDateTime.now();
        TripAggregate trip = TripAggregate.builder()
                .id(UUID.randomUUID().toString())
                .routeId(route.getId())
                .merchantId(command.merchantId())
                .tripCode(tripCode)
                .pickupBranch(command.pickupBranch())
                .departureTime(command.departureTime())
                .rawDepartureTime(command.rawDepartureTime())
                .rawDepartureDate(command.rawDepartureDate())
                .status(TripStatus.SCHEDULED)
                .createdAt(now)
                .createdBy(command.merchantId())
                .updatedAt(now)
                .updatedBy(command.merchantId())
                .build();

        sLog.info("Trip: {}", trip);
        tripAggregateRepositoryPort.save(trip);
        return toCreateResult(trip);
    }

    @Override
    @Transactional
    public UpdateTripResult updateTrip(UpdateTripCommand command) {
        TripAggregate existing = findTrip(command.tripId(), command.merchantId(), command.context().requestId(),
                command.context().requestDateTime(), command.context().channel());

        if (command.routeId() != null && !command.routeId().isBlank() && !command.routeId().equals(existing.getRouteId())) {
            findRoute(command.routeId(), command.merchantId(), command.context().requestId(),
                    command.context().requestDateTime(), command.context().channel());

            if (tripAggregateRepositoryPort.existsByRouteId(command.routeId().trim(), command.merchantId())) {
                throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(TRIP_ALREADY_EXISTS_FOR_ROUTE, command.routeId())));
            }
        }

        TripAggregate updated = existing.toBuilder()
                .routeId(ApiRequestUtils.firstNonBlank(command.routeId(), existing.getRouteId()))
                .merchantId(existing.getMerchantId())
                .tripCode(existing.getTripCode())
                .departureTime(command.departureTime() == null ? existing.getDepartureTime() : command.departureTime())
                .rawDepartureTime(ApiRequestUtils.firstNonBlank(command.rawDepartureTime(), existing.getRawDepartureTime()))
                .rawDepartureDate(ApiRequestUtils.firstNonBlank(command.rawDepartureDate(), existing.getRawDepartureDate()))
                .pickupBranch(ApiRequestUtils.firstNonBlank(command.pickupBranch(), existing.getPickupBranch()))
                .status(existing.getStatus())
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.merchantId())
                .build();

        tripAggregateRepositoryPort.save(updated);
        return toUpdateResult(updated);
    }

    @Override
    @Transactional
    public DeleteTripResult deleteTrip(DeleteTripCommand command) {
        TripAggregate existing = findTrip(command.tripId(), command.merchantId(), command.context().requestId(),
                command.context().requestDateTime(), command.context().channel());

        TripAggregate cancelled = existing.toBuilder()
                .status(TripStatus.CANCELLED)
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.merchantId())
                .build();

        tripAggregateRepositoryPort.save(cancelled);
        return DeleteTripResult.builder()
                .tripId(cancelled.getId())
                .status(cancelled.getStatus())
                .build();
    }

    @Override
    public FetchTripDetailResult fetchDetail(FetchTripDetailQuery query) {
        TripAggregate trip = findTrip(query.tripId(), query.merchantId(), query.context().requestId(),
                query.context().requestDateTime(), query.context().channel());

        RouteAggregate route = routeAggregateRepositoryPort.findById(trip.getRouteId())
                .orElseThrow(() -> new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, trip.getRouteId()))));

        if (query.status() != null && query.status() != trip.getStatus()) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(TRIP_NOT_FOUND, query.tripId())));
        }

        return toFetchDetailResult(trip, route);
    }

    @Override
    public FetchTripListResult fetchTripList(FetchTripListQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        validatePaging(query, pageSize, pageNumber);

        PagedResult<TripAggregate> page;
        if(query.status() != null) {
            page = tripAggregateRepositoryPort.fetch(query.context().merchantId(), query.status(), pageNumber - 1, pageSize);
        } else {
            page = tripAggregateRepositoryPort.fetch(query.context().merchantId(), pageNumber - 1, pageSize);
        }
        List<String> routeIds = page.getItems().stream()
                .map(TripAggregate::getRouteId)
                .distinct()
                .filter(Objects::nonNull)
                .toList();

        List<RouteAggregate> routeAggregateList = routeAggregateRepositoryPort.findByIdIn(routeIds);

        Map<String, RouteAggregate> routeMap = routeAggregateList.stream()
                .collect(Collectors.toMap(
                        RouteAggregate::getId,
                        route -> route
                ));

        return FetchTripListResult.builder()
                .items(page.getItems().stream()
                        .map(item -> {
                            RouteAggregate route = routeMap.get(item.getRouteId());
                            return toFetchDetailResult(item, route);
                        })
                        .toList())
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public AssignRouteResult assignRoute(AssignRouteCommand command) {
        if (tripAssignmentRepositoryPort.existsActiveByTripId(command.tripId(), command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_ROUTE_ASSIGNMENT, command.tripId())));
        }

        VehicleProfile vehicle = vehicleProfileRepositoryPort.findById(command.vehicleId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_NOT_FOUND)));

        TripAggregate trip = tripAggregateRepositoryPort.findById(command.tripId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(TRIP_NOT_FOUND, command.tripId()))));

        RouteAggregate route = routeAggregateRepositoryPort.findById(trip.getRouteId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, trip.getRouteId()))));

        VehicleTemplate vehicleTemplate = vehicleTemplateRepositoryPort.findById(vehicle.getTemplateId(), command.merchantId())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, VEHICLE_TEMPLATE_NOT_FOUND)));

        OffsetDateTime assignedAt = OffsetDateTime.now();
        TripAssignmentRecord routeAssignment = TripAssignmentRecord.assign(
                UUID.randomUUID().toString(),
                command.tripId(),
                command.creator(),
                trip.getMerchantId(),
                vehicle.getId(),
                command.driverId(),
                vehicleTemplate.getTicketPrice(),
                assignedAt
        );
        tripAssignmentRepositoryPort.save(routeAssignment);

        sLog.info("[ASSIGN-ROUTE] Trip Assigned successfully with vehicleId: {} driverId: {}", vehicle.getId(), command.driverId());

        TripSellableEvent sellableEvent = TripSellableEvent
                .builder()
                .tripId(routeAssignment.getTripId())
                .vehicleId(routeAssignment.getVehicleId())
                .assignedBy(command.creator())
                .assignedAt(routeAssignment.getAssignedAt())
                .status(TripStatus.ASSIGNED)
                .seatCount(vehicleTemplate.getSeatCapacity())
                .hasFloor(vehicle.isHasFloor())
                .creator(command.creator())
                .build();

        outBoxService.generateEvent(routeAssignment.getTripId(), tripTopic, tripReadyForSaleEvent, routeAssignment.getId(), sellableEvent, ApiRequestUtils.getHeader(command.context()));

        TripAssignedEvent assignedEvent = TripAssignedEvent
                .builder()
                .tripId(routeAssignment.getTripId())
                .driverId(routeAssignment.getDriverId())
                .vehicleId(routeAssignment.getVehicleId())
                .originName(route.getOriginName())
                .destinationName(route.getDestinationName())
                .departureTime(trip.getDepartureTime())
                .status(trip.getStatus())
                .assignedBy(command.creator())
                .assignedAt(routeAssignment.getAssignedAt())
                .build();

        outBoxService.generateEvent(routeAssignment.getTripId(), tripTopic, tripAssignedEvent, routeAssignment.getId(), assignedEvent, ApiRequestUtils.getHeader(command.context()));

        return AssignRouteResult.builder()
                .creator(command.creator())
                .assignedAt(routeAssignment.getAssignedAt().toString())
                .tripId(routeAssignment.getTripId())
                .vehicleId(routeAssignment.getVehicleId())
                .driverId(routeAssignment.getDriverId())
                .status(routeAssignment.getStatus().name())
                .build();
    }


    private void validatePaging(FetchTripListQuery query, int pageSize, int pageNumber) {
        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }
    }

    private RouteAggregate findRoute(String routeId, String merchantId, String requestId, String requestDateTime, String channel) {
        return routeAggregateRepositoryPort.findById(routeId, merchantId)
                .orElseThrow(() -> new BusinessException(requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(ROUTE_NOT_FOUND, routeId))));
    }

    private TripAggregate findTrip(String tripId, String merchantId, String requestId, String requestDateTime, String channel) {
        return tripAggregateRepositoryPort.findById(tripId, merchantId)
                .orElseThrow(() -> new BusinessException(requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(TRIP_NOT_FOUND, tripId))));
    }

    private CreateTripResult toCreateResult(TripAggregate trip) {
        return CreateTripResult.builder()
                .tripId(trip.getId())
                .routeId(trip.getRouteId())
                .pickupBranch(trip.getPickupBranch())
                .merchantId(trip.getMerchantId())
                .departureTime(trip.getDepartureTime())
                .rawDepartureTime(trip.getRawDepartureTime())
                .rawDepartureDate(trip.getRawDepartureDate())
                .status(trip.getStatus())
                .build();
    }

    private UpdateTripResult toUpdateResult(TripAggregate trip) {
        return UpdateTripResult.builder()
                .tripId(trip.getId())
                .routeId(trip.getRouteId())
                .pickupBranch(trip.getPickupBranch())
                .merchantId(trip.getMerchantId())
                .departureTime(trip.getDepartureTime())
                .rawDepartureTime(trip.getRawDepartureTime())
                .rawDepartureDate(trip.getRawDepartureDate())
                .status(trip.getStatus())
                .build();
    }

    private FetchTripDetailResult toFetchDetailResult(TripAggregate trip, RouteAggregate route) {
        return FetchTripDetailResult.builder()
                .tripId(trip.getId())
                .creator(trip.getCreator())
                .tripCode(trip.getTripCode())
                .pickupBranch(trip.getPickupBranch())
                .departureTime(trip.getDepartureTime())
                .rawDepartureTime(trip.getRawDepartureTime())
                .rawDepartureDate(trip.getRawDepartureDate())
                .rawArrivalTime(calculateArrivalTime(trip.getRawDepartureTime(), route.getDuration()))
                .status(trip.getStatus())
                .route(FetchTripDetailResult.FetchTripDetailRoute.builder()
                        .routeId(route.getId())
                        .originName(route.getOriginName())
                        .originCode(route.getOriginCode())
                        .originDepartmentId(route.getOriginDepartmentId())
                        .destinationName(route.getDestinationName())
                        .destinationCode(route.getDestinationCode())
                        .destinationDepartmentId(route.getDestinationDepartmentId())
                        .duration(route.getDuration())
                        .build())
                .build();
    }

    private String calculateArrivalTime(String rawDepartureTime, Long durationMinutes) {
        if (rawDepartureTime == null || durationMinutes == null) return null;
        try {
            String[] parts = rawDepartureTime.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            int totalMinutes = hours * 60 + minutes + durationMinutes.intValue();
            int arrivalHours = (totalMinutes / 60) % 24;
            int arrivalMinutes = totalMinutes % 60;

            return String.format("%02d:%02d", arrivalHours, arrivalMinutes);
        } catch (Exception e) {
            return null;
        }
    }
}
