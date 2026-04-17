package vn.com.routex.merchant.platform.interfaces.model.vehicletemplate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteVehicleTemplateResponse extends BaseResponse<DeleteVehicleTemplateResponse.DeleteVehicleTemplateResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteVehicleTemplateResponseData {
        private String id;
        private String code;
        private VehicleTemplateStatus status;
    }
}
