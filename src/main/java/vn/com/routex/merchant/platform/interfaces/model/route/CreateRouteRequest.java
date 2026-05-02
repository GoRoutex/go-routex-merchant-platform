package vn.com.routex.merchant.platform.interfaces.model.route;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.util.List;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.RegexConstant.OFFSET_DATE_TIME_REGEX;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateRouteRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateRouteRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateRouteRequestData {

        @NotBlank
        @NotNull
        private String creator;

        @NotBlank
        @NotNull
        private String originName;

        @NotBlank
        @NotNull
        private String destinationName;

        private List<RoutePoints> operationPoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class RoutePoints {
        private String operationOrder;
        private String note;
        private String operationPointId;
        private String stopName;
        private String stopAddress;
        private String stopCity;
        private Double stopLatitude;
        private Double stopLongitude;
    }
}
