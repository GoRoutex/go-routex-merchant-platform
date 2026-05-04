package vn.com.routex.merchant.platform.interfaces.model.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateTripResponse extends BaseResponse<UpdateTripResponse.UpdateTripResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateTripResponseData {
        private String tripId;
        private String routeId;
        private String merchantId;
        private OffsetDateTime departureTime;
        private String rawDepartureTime;
        private String rawDepartureDate;
        private Long durationMinutes;
        private TripStatus status;
    }
}
