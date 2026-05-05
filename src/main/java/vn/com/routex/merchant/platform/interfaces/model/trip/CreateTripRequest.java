package vn.com.routex.merchant.platform.interfaces.model.trip;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.time.OffsetDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateTripRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateTripRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateTripRequestData {
        private String routeId;
        private OffsetDateTime departureTime;
        private String pickupBranch;
        private String rawDepartureTime;
        private String rawDepartureDate;
    }
}
