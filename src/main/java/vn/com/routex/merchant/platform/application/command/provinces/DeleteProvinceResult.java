package vn.com.routex.merchant.platform.application.command.provinces;

import lombok.Builder;

@Builder
public record DeleteProvinceResult(
        Integer id
) {
}

