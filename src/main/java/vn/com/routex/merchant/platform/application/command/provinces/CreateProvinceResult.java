package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;

@Builder
public record CreateProvinceResult(
        Integer id,
        String name,
        String code
) {
}

