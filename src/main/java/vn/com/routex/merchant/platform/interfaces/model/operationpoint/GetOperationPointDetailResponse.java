package vn.com.routex.merchant.platform.interfaces.model.operationpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointStatus;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class GetOperationPointDetailResponse extends BaseResponse<GetOperationPointDetailResponse.GetOperationPointDetailResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetOperationPointDetailResponseData {
        private String id;
        private String code;
        private String name;
        private OperationPointType type;
        private String address;
        private String city;
        private Double latitude;
        private Double longitude;
        private OperationPointStatus status;
    }
}
