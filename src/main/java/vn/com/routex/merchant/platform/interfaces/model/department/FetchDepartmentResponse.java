package vn.com.routex.merchant.platform.interfaces.model.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchDepartmentResponse extends BaseResponse<FetchDepartmentResponse.FetchDepartmentResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchDepartmentResponsePage {
        private List<FetchDepartmentResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchDepartmentResponseData {
        private String id;
        private String code;
        private String name;
        private DepartmentType type;
        private String address;
        private String wardId;
        private String wardName;
        private String districtId;
        private String districtName;
        private String provinceId;
        private String provinceName;
        private boolean isShuttleService;
        private Double latitude;
        private Double longitude;
        private DepartmentStatus status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}

