package vn.com.routex.merchant.platform.interfaces.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchRouteResponse extends BaseResponse<FetchRouteResponse.FetchRouteResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponsePage {
        private List<FetchRouteResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponseData {
        private String id;
        private String creator;
        private String originCode;
        private String originName;
        private String destinationCode;
        private String destinationName;
        private RouteStatus status;
        private List<SearchRouteResponse.SearchRoutePoints> routePoints;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
