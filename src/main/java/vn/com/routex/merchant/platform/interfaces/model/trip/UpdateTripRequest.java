package vn.com.routex.merchant.platform.interfaces.model.trip;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class UpdateTripRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateTripRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateTripRequestData {
        @NotNull
        @NotBlank
        private String tripId;

        private String routeId;
        private OffsetDateTime departureTime;
        private String rawDepartureTime;
        private String rawDepartureDate;
        private Long durationMinutes;
    }
}
