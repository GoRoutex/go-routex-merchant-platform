package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.route.model.RouteAggregate;
import vn.com.routex.merchant.platform.domain.route.model.RouteAssignmentRecord;
import vn.com.routex.merchant.platform.domain.route.model.RouteStopPlan;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleProfile;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.assignment.entity.RouteAssignmentEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.entity.RouteEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.routepoint.entity.RouteStopEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.vehicle.entity.VehicleEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;

@Component
public class RoutePersistenceMapper {

    public RouteAggregate toAggregate(RouteEntity route) {
        return RouteAggregate.builder()
                .id(route.getId())
                .merchantId(route.getMerchantId())
                .routeCode(route.getRouteCode())
                .creator(route.getCreator())
                .pickupBranch(route.getPickupBranch())
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .plannedStartTime(route.getPlannedStartTime())
                .plannedEndTime(route.getPlannedEndTime())
                .actualStartTime(route.getActualStartTime())
                .actualEndTime(route.getActualEndTime())
                .status(route.getStatus())
                .createdAt(route.getCreatedAt())
                .createdBy(route.getCreatedBy())
                .updatedAt(route.getUpdatedAt())
                .updatedBy(route.getUpdatedBy())
                .build();
    }

    public RouteEntity toEntity(RouteAggregate aggregate) {
        return RouteEntity.builder()
                .id(aggregate.getId())
                .merchantId(aggregate.getMerchantId())
                .routeCode(aggregate.getRouteCode())
                .creator(aggregate.getCreator())
                .pickupBranch(aggregate.getPickupBranch())
                .origin(aggregate.getOrigin())
                .destination(aggregate.getDestination())
                .plannedStartTime(aggregate.getPlannedStartTime())
                .plannedEndTime(aggregate.getPlannedEndTime())
                .actualStartTime(aggregate.getActualStartTime())
                .actualEndTime(aggregate.getActualEndTime())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .createdBy(aggregate.getCreatedBy())
                .updatedAt(aggregate.getUpdatedAt())
                .updatedBy(aggregate.getUpdatedBy())
                .build();
    }

    public RouteStopPlan toStopPlan(RouteStopEntity routeStop) {
        return RouteStopPlan.builder()
                .id(routeStop.getId())
                .routeId(routeStop.getRouteId())
                .creator(routeStop.getCreator())
                .stopOrder(Integer.parseInt(routeStop.getStopOrder()))
                .plannedArrivalTime(routeStop.getPlannedArrivalTime())
                .plannedDepartureTime(routeStop.getPlannedDepartureTime())
                .note(routeStop.getNote())
                .operationPointId(routeStop.getOperationPointId())
                .stopName(routeStop.getStopName())
                .stopAddress(routeStop.getStopAddress())
                .stopCity(routeStop.getStopCity())
                .stopLatitude(routeStop.getStopLatitude())
                .stopLongitude(routeStop.getStopLongitude())
                .createdAt(routeStop.getCreatedAt())
                .createdBy(routeStop.getCreatedBy())
                .build();
    }

    public RouteStopEntity toEntity(RouteStopPlan stopPlan) {
        return RouteStopEntity.builder()
                .id(stopPlan.getId())
                .routeId(stopPlan.getRouteId())
                .creator(stopPlan.getCreator())
                .stopOrder(String.valueOf(stopPlan.getStopOrder()))
                .plannedArrivalTime(stopPlan.getPlannedArrivalTime())
                .plannedDepartureTime(stopPlan.getPlannedDepartureTime())
                .note(stopPlan.getNote())
                .operationPointId(stopPlan.getOperationPointId())
                .stopName(stopPlan.getStopName())
                .stopAddress(stopPlan.getStopAddress())
                .stopCity(stopPlan.getStopCity())
                .stopLatitude(stopPlan.getStopLatitude())
                .stopLongitude(stopPlan.getStopLongitude())
                .createdAt(stopPlan.getCreatedAt())
                .createdBy(stopPlan.getCreatedBy())
                .build();
    }

    public RouteAssignmentRecord toAssignmentRecord(RouteAssignmentEntity assignment) {
        return RouteAssignmentRecord.builder()
                .id(assignment.getId())
                .merchantId(assignment.getMerchantId())
                .routeId(assignment.getRouteId())
                .creator(assignment.getCreator())
                .driverId(assignment.getDriverId())
                .vehicleId(assignment.getVehicleId())
                .assignedAt(assignment.getAssignedAt())
                .unAssignedAt(assignment.getUnAssignedAt())
                .status(assignment.getStatus())
                .ticketPrice(assignment.getTicketPrice())
                .createdAt(assignment.getCreatedAt())
                .createdBy(assignment.getCreatedBy())
                .updatedAt(assignment.getUpdatedAt())
                .updatedBy(assignment.getUpdatedBy())
                .build();
    }

    public RouteAssignmentEntity toEntity(RouteAssignmentRecord record) {
        return RouteAssignmentEntity.builder()
                .id(record.getId())
                .merchantId(record.getMerchantId())
                .routeId(record.getRouteId())
                .creator(record.getCreator())
                .driverId(record.getDriverId())
                .vehicleId(record.getVehicleId())
                .assignedAt(record.getAssignedAt())
                .unAssignedAt(record.getUnAssignedAt())
                .status(record.getStatus())
                .ticketPrice(record.getTicketPrice())
                .createdAt(record.getCreatedAt())
                .createdBy(record.getCreatedBy())
                .updatedAt(record.getUpdatedAt())
                .updatedBy(record.getUpdatedBy())
                .build();
    }

    public VehicleProfile toVehicleProfile(VehicleEntity vehicle, VehicleTemplateEntity template) {
        return VehicleProfile.builder()
                .id(vehicle.getId())
                .vehiclePlate(vehicle.getVehiclePlate())
                .templateId(template.getId())
                .hasFloor(template != null && template.isHasFloor())
                .build();
    }
}
