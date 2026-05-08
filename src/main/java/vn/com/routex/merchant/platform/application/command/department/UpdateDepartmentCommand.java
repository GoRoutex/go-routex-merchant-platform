package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.DepartmentType;

@Builder
public record UpdateDepartmentCommand(
        RequestContext context,
        String merchantId,
        String id,
        String name,
        DepartmentType type,
        String address,
        String wardId,
        String provinceId,
        String openingTime,
        String closingTime,
        String onlineOpeningTime,
        String onlineClosingTime,
        Double latitude,
        Double longitude,
        DepartmentStatus status
) {
}
