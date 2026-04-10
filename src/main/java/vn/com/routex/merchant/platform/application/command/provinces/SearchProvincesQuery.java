package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;

@Builder
public record SearchProvincesQuery(
        String merchantId,
        String keyword,
        int page,
        int size
) {
}

