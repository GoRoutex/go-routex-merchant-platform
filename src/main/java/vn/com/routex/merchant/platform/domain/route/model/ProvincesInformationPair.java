package vn.com.routex.merchant.platform.domain.route.model;

import lombok.Builder;

@Builder
public record ProvincesInformationPair(
        String originCode,
        String destinationCode,
        String originName,
        String destinationName) {
}
