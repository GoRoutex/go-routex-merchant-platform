package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointStatus;

@Builder
public record DeleteOperationPointResult(
        String id,
        String code,
        OperationPointStatus status
) {
}
