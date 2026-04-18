package vn.com.routex.merchant.platform.interfaces.model.maintenance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteMaintenancePlanResponse extends BaseResponse<DeleteMaintenancePlanResponse.DeleteMaintenancePlanResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteMaintenancePlanResponseData {
        private String id;
        private String code;
        private MaintenancePlanStatus status;
    }
}
