package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.application.service.InternalBookingContextService;
import vn.com.routex.merchant.platform.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.merchant.platform.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.seat.model.SeatTemplate;
import vn.com.routex.merchant.platform.domain.seat.port.SeatTemplateRepositoryPort;
import vn.com.routex.merchant.platform.domain.trip.model.TripAggregate;
import vn.com.routex.merchant.platform.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleProfile;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleProfileRepositoryPort;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.merchant.platform.interfaces.model.internal.booking.InternalBookingContextResponses;

import java.util.List;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.ROUTE_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.TRIP_ASSIGNMENT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.TRIP_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.VEHICLE_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class InternalBookingContextServiceImpl implements InternalBookingContextService {

    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;
    private final TripAssignmentRepositoryPort tripAssignmentRepositoryPort;
    private final RouteAggregateRepositoryPort routeAggregateRepositoryPort;
    private final VehicleProfileRepositoryPort vehicleProfileRepositoryPort;
    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;
    private final SeatTemplateRepositoryPort seatTemplateRepositoryPort;

    @Override
    public InternalBookingContextResponses.TripBookingContextData fetchTripBookingContext(String tripId, RequestContext context) {
        TripAggregate trip = tripAggregateRepositoryPort.findById(tripId)
                .orElseThrow(() -> notFound(context, String.format(TRIP_NOT_FOUND, tripId)));

        TripAssignmentRecord assignment = tripAssignmentRepositoryPort.findActiveByTripId(tripId)
                .orElseThrow(() -> notFound(context, TRIP_ASSIGNMENT_NOT_FOUND));

        RouteAggregate route = routeAggregateRepositoryPort.findById(trip.getRouteId())
                .orElseThrow(() -> notFound(context, String.format(ROUTE_NOT_FOUND, trip.getRouteId())));

        return InternalBookingContextResponses.TripBookingContextData.builder()
                .tripId(trip.getId())
                .routeId(route.getId())
                .merchantId(trip.getMerchantId() == null || trip.getMerchantId().isBlank() ? route.getMerchantId() : trip.getMerchantId())
                .vehicleId(assignment.getVehicleId())
                .ticketPrice(assignment.getTicketPrice())
                .pickupBranch(trip.getPickupBranch())
                .originName(route.getOriginName())
                .destinationName(route.getDestinationName())
                .routeStatus(route.getStatus() == null ? null : route.getStatus().name())
                .tripStatus(trip.getStatus() == null ? null : trip.getStatus().name())
                .build();
    }

    @Override
    public InternalBookingContextResponses.VehicleSeatBlueprintData fetchVehicleSeatBlueprint(String vehicleId, RequestContext context) {
        VehicleProfile vehicle = vehicleProfileRepositoryPort.findById(vehicleId)
                .orElseThrow(() -> notFound(context, String.format(VEHICLE_NOT_FOUND_BY_ID, vehicleId)));

        VehicleTemplate template = vehicleTemplateRepositoryPort.findById(vehicle.getTemplateId())
                .orElseThrow(() -> notFound(context, String.format("Vehicle template with Id %s not found", vehicle.getTemplateId())));

        List<SeatTemplate> seatTemplates = seatTemplateRepositoryPort.findByVehicleTemplateId(template.getId());

        if (seatTemplates.isEmpty()) {
            throw notFound(context, String.format("Seat template for vehicle template %s not found", template.getId()));
        }

        return InternalBookingContextResponses.VehicleSeatBlueprintData.builder()
                .vehicleId(vehicle.getId())
                .merchantId(vehicle.getMerchantId())
                .templateId(template.getId())
                .seatCapacity(template.getSeatCapacity())
                .hasFloor(template.isHasFloor())
                .vehicleStatus(vehicle.getStatus())
                .seats(seatTemplates.stream()
                        .map(seat -> InternalBookingContextResponses.SeatBlueprintItem.builder()
                                .id(seat.getId())
                                .seatCode(seat.getSeatCode())
                                .floor(seat.getFloor())
                                .rowNo(seat.getRowNo())
                                .columnNo(seat.getColumnNo())
                                .build())
                        .toList())
                .build();
    }

    private BusinessException notFound(RequestContext context, String message) {
        return new BusinessException(
                context.requestId(),
                context.requestDateTime(),
                context.channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, message)
        );
    }
}
