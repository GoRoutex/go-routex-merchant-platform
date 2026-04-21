package vn.com.routex.merchant.platform.interfaces.model.vehicletemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.vehicle.FuelType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchVehicleTemplateResponse extends BaseResponse<FetchVehicleTemplateResponse.FetchVehicleTemplateResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchVehicleTemplateResponsePage {
        private List<FetchVehicleTemplateResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchVehicleTemplateResponseData {
        private String id;
        private String merchantId;
        private String code;
        private String name;
        private String manufacturer;
        private String model;
        private Long seatCapacity;
        private VehicleTemplateCategory category;
        private VehicleTemplateType type;
        private FuelType fuelType;
        private Boolean hasFloor;
        private BigDecimal ticketPrice;
        private VehicleTemplateStatus status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
