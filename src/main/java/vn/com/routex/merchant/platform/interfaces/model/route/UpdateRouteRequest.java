package vn.com.routex.merchant.platform.interfaces.model.route;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateRouteRequest extends BaseRequest {

    private String routeId;
    private String creator;

    @Valid
    @NotNull
    private UpdateRouteRequestData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateRouteRequestData {
        private String originName;
        private String destinationName;
        private RouteStatus status;
        private Long duration;
        private List<UpdateRoutePointRequest> routePoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @SuperBuilder
    @NoArgsConstructor
    public static class UpdateRoutePointRequest {
        private String id;
        private String operationOrder;
        private String routeId;
        private String note;
    }
}
