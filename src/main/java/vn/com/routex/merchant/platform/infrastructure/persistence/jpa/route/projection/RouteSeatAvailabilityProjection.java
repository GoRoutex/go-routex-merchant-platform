package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.route.projection;

public interface RouteSeatAvailabilityProjection {

    String getRouteId();

    Long getAvailableSeat();
}

