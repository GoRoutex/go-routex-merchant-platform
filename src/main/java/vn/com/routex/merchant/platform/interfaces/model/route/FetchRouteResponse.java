package vn.com.routex.merchant.platform.interfaces.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchRouteResponse extends BaseResponse<FetchRouteResponse.FetchRouteResponsePage> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponsePage {
        private List<FetchRouteResponseData> items;
        private Pagination pagination;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchRouteResponseData {
        private String id;
        private String creator;
        private String pickupBranch;
        private String routeCode;
        private String origin;
        private String destination;
        private OffsetDateTime plannedStartTime;
        private OffsetDateTime plannedEndTime;
        private OffsetDateTime actualStartTime;
        private OffsetDateTime actualEndTime;
        private String status;
        private Long availableSeats;
        private String vehicleId;
        private String vehiclePlate;
        private Boolean hasFloor;
        private OffsetDateTime assignedAt;
        private AssignmentInformation assignmentInformation;
        private List<SearchRouteResponse.SearchRoutePoints> routePoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AssignmentInformation {
        private String vehicleId;
        private String vehiclePlate;
        private String vehicleTemplateName;
        private String driverId;
        private String driverName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
    }
}
