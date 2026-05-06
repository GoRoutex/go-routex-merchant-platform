package vn.com.routex.merchant.platform.application.command.department;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;

@Builder
public record DeleteDepartmentResult(
        String id,
        String code,
        DepartmentStatus status
) {
}
