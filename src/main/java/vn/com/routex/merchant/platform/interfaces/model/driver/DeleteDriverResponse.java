package vn.com.routex.merchant.platform.interfaces.model.driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.OperationStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteDriverResponse extends BaseResponse<DeleteDriverResponse.DeleteDriverResponseData> {
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteDriverResponseData {
        private String id;
        private DriverStatus status;
        private OperationStatus operationStatus;
    }
}
