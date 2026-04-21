package vn.com.routex.merchant.platform.interfaces.model.vehicletemplate;

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

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateVehicleTemplateResponse extends BaseResponse<UpdateVehicleTemplateResponse.UpdateVehicleTemplateResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateVehicleTemplateResponseData {
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
}
