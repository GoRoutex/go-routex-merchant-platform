package vn.com.routex.merchant.platform.domain.booking.port;



import vn.com.routex.merchant.platform.domain.booking.model.Booking;

import java.util.Optional;

public interface BookingRepositoryPort {

    Optional<Booking> findById(String bookingId);

    Optional<Booking> findById(String bookingId, String merchantId);

    Booking save(Booking booking);

    String generateBookingCode();
}
