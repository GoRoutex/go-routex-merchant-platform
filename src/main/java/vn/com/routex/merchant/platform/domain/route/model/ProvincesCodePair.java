package vn.com.routex.merchant.platform.domain.route.model;

import lombok.Builder;

@Builder
public record ProvincesCodePair(String originCode, String destinationCode) {
}
