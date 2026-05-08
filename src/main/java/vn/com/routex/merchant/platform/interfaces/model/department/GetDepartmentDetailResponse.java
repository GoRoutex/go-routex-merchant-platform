package vn.com.routex.merchant.platform.interfaces.model.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class GetDepartmentDetailResponse extends BaseResponse<GetDepartmentDetailResponse.GetDepartmentDetailResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class GetDepartmentDetailResponseData {
        private String id;
        private String name;
        private DepartmentType type;
        private String address;
        private String wardId;
        private String wardName;
        private String provinceId;
        private String provinceName;
        private String openingTime;
        private String closingTime;
        private String onlineOpeningTime;
        private String onlineClosingTime;
        private Double latitude;
        private Double longitude;
        private DepartmentStatus status;
    }
}
