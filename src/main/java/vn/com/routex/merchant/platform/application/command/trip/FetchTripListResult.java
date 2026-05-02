package vn.com.routex.merchant.platform.application.command.trip;

import lombok.Builder;

import java.util.List;

@Builder
public record FetchTripListResult(
        List<FetchTripDetailResult> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
