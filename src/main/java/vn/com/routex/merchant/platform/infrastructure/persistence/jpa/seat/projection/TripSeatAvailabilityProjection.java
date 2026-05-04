package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.seat.projection;

public interface TripSeatAvailabilityProjection {

    String getTripId();

    Long getAvailableSeat();
}

