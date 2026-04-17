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

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CreateVehicleTemplateResponse extends BaseResponse<CreateVehicleTemplateResponse.CreateVehicleTemplateResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateVehicleTemplateResponseData {
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
        private VehicleTemplateStatus status;
    }
}
