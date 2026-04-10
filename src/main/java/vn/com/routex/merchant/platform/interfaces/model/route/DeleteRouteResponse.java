package vn.com.routex.merchant.platform.interfaces.model.route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteRouteResponse extends BaseResponse<DeleteRouteResponse.DeleteRouteResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteRouteResponseData {
        private String creator;
        private String routeId;
        private String routeCode;
        private String status;
        private OffsetDateTime updatedAt;
    }
}
