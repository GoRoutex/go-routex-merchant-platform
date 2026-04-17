package vn.com.routex.merchant.platform.application.command.vehicle;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;

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
            String templateId,
            String creator,
            VehicleStatus status,
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            String vehiclePlate,
            Long seatCapacity,
            Boolean hasFloor,
            String manufacturer
    ) {
    }
}
