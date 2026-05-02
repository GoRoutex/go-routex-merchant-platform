package vn.com.routex.merchant.platform.interfaces.model.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateTripResponse extends BaseResponse<CreateTripResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateTripResponseData {
        private String routeId;
        private OffsetDateTime departureTime;
        private String rawDepartureTime;
        private String rawDepartureDate;
        private Long durationMinutes;
    }
}
