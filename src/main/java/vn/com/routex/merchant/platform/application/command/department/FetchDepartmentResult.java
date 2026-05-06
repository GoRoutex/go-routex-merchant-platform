package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;

import java.util.List;

@Builder
public record FetchDepartmentResult(
        List<FetchDepartmentItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchDepartmentItemResult(
            String id,
            String code,
            String name,
            DepartmentType type,
            String address,
            String wardId,
            String wardName,
            String districtId,
            String districtName,
            String provinceId,
            String provinceName,
            boolean isShuttleService,
            Double latitude,
            Double longitude,
            DepartmentStatus status
    ) {
    }
}

