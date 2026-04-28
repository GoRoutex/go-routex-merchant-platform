package vn.com.routex.merchant.platform.domain.seat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.seat.SeatStatus;

/**
 * Domain model for route seat.
 * Persistence concerns (JPA annotations, table/column mapping) live in infrastructure layer:
 * {@code infrastructure.persistence.jpa.route.entity.RouteSeatEntity}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RouteSeat {
    private String id;
    private String routeId;
    private String seatNo;
    private SeatStatus status;
    private String creator;
    private String seatTemplateId;
}

