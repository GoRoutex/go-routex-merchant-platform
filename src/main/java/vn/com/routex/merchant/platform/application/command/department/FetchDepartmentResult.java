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
            String name,
            DepartmentType type,
            String address,
            String wardId,
            String wardName,
            String provinceId,
            String provinceName,
            String openingTime,
            String closingTime,
            String onlineOpeningTime,
            String onlineClosingTime,
            Double latitude,
            Double longitude,
            DepartmentStatus status
    ) {
    }
}

