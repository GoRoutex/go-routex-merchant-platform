package vn.com.routex.merchant.platform.interfaces.model.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteTripResponse extends BaseResponse<DeleteTripResponse.DeleteTripResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteTripResponseData {
        private String tripId;
        private TripStatus status;
    }
}
