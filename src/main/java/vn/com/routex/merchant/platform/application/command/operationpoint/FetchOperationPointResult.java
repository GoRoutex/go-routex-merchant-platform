package vn.com.routex.merchant.platform.application.command.operationpoint;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointStatus;
import vn.com.routex.merchant.platform.domain.operationpoint.OperationPointType;

import java.util.List;

@Builder
public record FetchOperationPointResult(
        List<FetchOperationPointItemResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {

    @Builder
    public record FetchOperationPointItemResult(
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
}

