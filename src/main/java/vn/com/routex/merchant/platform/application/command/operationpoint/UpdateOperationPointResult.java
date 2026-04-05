package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointStatus;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointType;

@Builder
public record UpdateOperationPointResult(
        String id,
        String code,
        String name,
        OperationPointType type,
        String address,
        String city,
        Double latitude,
        Double longitude,
        OperationPointStatus status
) {
}
