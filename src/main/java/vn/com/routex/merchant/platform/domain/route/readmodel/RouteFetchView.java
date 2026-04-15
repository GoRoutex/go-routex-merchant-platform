package vn.com.routex.merchant.platform.domain.route.readmodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.routex.merchant.platform.application.command.route.RoutePointResult;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouteFetchView {
    private String id;
    private String routeCode;
    private String creator;
    private String pickupBranch;
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
    private List<RoutePointResult> routePoints;
}

