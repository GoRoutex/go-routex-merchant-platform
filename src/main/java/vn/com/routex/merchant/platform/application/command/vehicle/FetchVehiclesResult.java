package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleType;

import java.util.List;

@Builder
public record FetchVehiclesResult(
        List<FetchVehicleItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchVehicleItemResult(
            String id,
            String creator,
            VehicleStatus status,
            VehicleType type,
            String vehiclePlate,
            Integer seatCapacity,
            Boolean hasFloor,
            String manufacturer
    ) {
    }
}

