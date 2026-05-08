package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;

@Builder
public record CreateDepartmentResult(
        String id,
        String name,
        DepartmentType type,
        String address,
        String wardId,
        String wardName,
        String provinceId,
        String provinceName,
        String onlineOpeningTime,
        String onlineClosingTime,
        String openingTime,
        String closingTime,
        Double latitude,
        Double longitude,
        DepartmentStatus status
) {
}
