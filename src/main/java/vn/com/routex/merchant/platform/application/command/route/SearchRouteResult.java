package vn.com.routex.merchant.platform.application.command.route;

import lombok.Builder;

import java.util.List;

@Builder
public record SearchRouteResult(
        List<SearchRouteItemResult> data
) {
}
