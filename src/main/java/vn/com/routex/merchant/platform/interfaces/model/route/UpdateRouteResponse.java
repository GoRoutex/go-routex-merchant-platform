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
public class UpdateRouteResponse extends BaseResponse<UpdateRouteResponse.UpdateRouteResponseData> {

    private String routeId;
    private String creator;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateRouteResponseData {
        private String originCode;
        private String originName;
        private String destinationCode;
        private String destinationName;
        private Long duration;
        private RouteStatus status;
        private List<UpdateRoutePointResponse> routePoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateRoutePointResponse {
        private String id;
        private String operationOrder;
        private String note;
    }
}
