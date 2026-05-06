package vn.com.routex.merchant.platform.application.command.department;


import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;

@Builder
public record CreateDepartmentCommand(
        RequestContext context,
        String merchantId,
        String code,
        String name,
        DepartmentType type,
        String address,
        String wardId,
        String districtId,
        String provinceId,
        Long timeAtDepartment,
        boolean isShuttleService,
        String openingTime,
        String closingTime,
        Double latitude,
        Double longitude,
        DepartmentStatus status
) {
}
