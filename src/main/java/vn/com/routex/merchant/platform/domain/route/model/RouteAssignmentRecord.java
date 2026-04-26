package vn.com.routex.merchant.platform.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.assignment.RouteAssignmentStatus;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RouteAssignmentRecord extends AbstractAuditingEntity {
    private String id;
    private String routeId;
    private String creator;
    private String merchantId;
    private String vehicleId;
    private String driverId;
    private BigDecimal ticketPrice;
    private OffsetDateTime assignedAt;
    private OffsetDateTime unAssignedAt;
    private RouteAssignmentStatus status;

    public static RouteAssignmentRecord assign(
            String id,
            String routeId,
            String creator,
            String merchantId,
            String vehicleId,
            String driverId,
            BigDecimal ticketPrice,
            OffsetDateTime assignedAt
    ) {
        return RouteAssignmentRecord.builder()
                .id(id)
                .routeId(routeId)
                .creator(creator)
                .merchantId(merchantId)
                .driverId(driverId)
                .vehicleId(vehicleId)
                .assignedAt(assignedAt)
                .ticketPrice(ticketPrice)
                .status(RouteAssignmentStatus.PENDING_ASSIGNMENT)
                .build();
    }

    public void complete(String actor, OffsetDateTime at) {
        this.status = RouteAssignmentStatus.COMPLETED;
        this.setUpdatedAt(at);
        this.setUpdatedBy(actor);
    }

    public void cancel(String actor, OffsetDateTime at) {
        this.status = RouteAssignmentStatus.CANCELED;
        this.unAssignedAt = at;
        this.setUpdatedAt(at);
        this.setUpdatedBy(actor);
    }
}
