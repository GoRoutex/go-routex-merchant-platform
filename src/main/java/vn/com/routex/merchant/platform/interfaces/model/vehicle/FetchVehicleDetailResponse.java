package vn.com.routex.merchant.platform.interfaces.model.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchVehicleDetailResponse extends BaseResponse<FetchVehicleDetailResponse.FetchVehicleDetailResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchVehicleDetailResponseData {
        private String id;
        private String merchantId;
        private String creator;
        private VehicleStatus status;
        private VehicleType type;
        private String vehiclePlate;
        private Integer seatCapacity;
        private Boolean hasFloor;
        private String manufacturer;
    }
}
