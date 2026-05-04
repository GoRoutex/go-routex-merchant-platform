package vn.com.routex.merchant.platform.interfaces.model.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchTripListResponse extends BaseResponse<FetchTripListResponse.FetchTripListResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTripListResponsePage {
        private List<FetchTripListResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchTripListResponseData {
        private String tripId;
        private String routeId;
        private String merchantId;
        private String tripCode;
        private OffsetDateTime departureTime;
        private String rawDepartureTime;
        private String rawDepartureDate;
        private Long durationMinutes;
        private TripStatus status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
