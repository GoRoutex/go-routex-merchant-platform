package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;

@Builder
public record CreateDepartmentResult(
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
        Long timeAtDepartment,
        boolean isShuttleService,
        String openingTime,
        String closingTime,
        Double latitude,
        Double longitude,
        DepartmentStatus status
) {
}
