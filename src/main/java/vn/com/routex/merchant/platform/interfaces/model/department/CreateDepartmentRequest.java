package vn.com.routex.merchant.platform.interfaces.model.department;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateDepartmentRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateDepartmentRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateDepartmentRequestData {
        private String code;
        private String name;
        private DepartmentType type;
        private String address;
        private String wardId;
        private String districtId;
        private String provinceId;
        private Long timeAtDepartment;
        private boolean isShuttleService;
        private String openingTime;
        private String closingTime;
        private Double latitude;
        private Double longitude;
        private DepartmentStatus status;
    }


}
