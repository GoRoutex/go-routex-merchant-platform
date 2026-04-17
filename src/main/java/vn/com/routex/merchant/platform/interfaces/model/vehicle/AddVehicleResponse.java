package vn.com.routex.merchant.platform.interfaces.model.vehicle;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AddVehicleResponse extends BaseResponse<AddVehicleResponse.AddVehicleResponseData> {
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class AddVehicleResponseData {
        private String id;
        private String templateId;
        private String creator;
        private VehicleTemplateCategory category;
        private VehicleTemplateType type;
        private String vehiclePlate;
        private Long seatCapacity;
        private String manufacturer;
        private VehicleStatus status;
    }
}
