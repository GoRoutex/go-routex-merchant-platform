package vn.com.routex.merchant.platform.interfaces.model.route;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.route.RouteStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchDetailRouteResponse extends BaseResponse<FetchDetailRouteResponse.FetchDetailRouteResponseData> {


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class FetchDetailRouteResponseData {
        private String id;
        private String creator;
        private String originCode;
        private String originName;
        private String destinationCode;
        private String destinationName;
        private RouteStatus status;
        private Long availableSeats;
        private String vehicleId;
        private String vehiclePlate;
        private Boolean hasFloor;
        private OffsetDateTime assignedAt;
        private List<SearchRouteResponse.SearchRoutePoints> routePoints;
    }
}
