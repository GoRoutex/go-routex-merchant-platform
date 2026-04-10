package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;

@Builder
public record CreateProvinceResult(
        int id,
        String name,
        String code
) {
}

