package vn.com.routex.merchant.platform.domain.route.readmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.application.command.route.RoutePointResult;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RouteFetchView {
    private String id;
    private String creator;
    private String originCode;
    private String originName;
    private String destinationCode;
    private String destinationName;
    private String status;
    private Long availableSeats;
    private Boolean hasFloor;
    private OffsetDateTime assignedAt;
    private AssignmentResult assignmentResult;
    private List<RoutePointResult> routePoints;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AssignmentResult {
        private String vehicleId;
        private String vehiclePlate;
        private String vehicleTemplateName;
        private String driverId;
        private String driverName;
    }
}

