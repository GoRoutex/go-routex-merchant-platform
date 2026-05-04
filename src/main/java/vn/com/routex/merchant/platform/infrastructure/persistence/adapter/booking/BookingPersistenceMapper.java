package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.booking;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.booking.model.Booking;
import vn.com.routex.merchant.platform.domain.booking.model.BookingSeat;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.booking.entity.BookingEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.booking.entity.BookingSeatEntity;

@Component
public class BookingPersistenceMapper {

    public Booking toDomain(BookingEntity entity) {
        return Booking.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                .tripId(entity.getTripId())
                .customerId(entity.getCustomerId())
                .seatCount(entity.getSeatCount())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .heldAt(entity.getHeldAt())
                .holdUntil(entity.getHoldUntil())
                .cancelledAt(entity.getCancelledAt())
                .note(entity.getNote())
                .creator(entity.getCreator())
                .build();
    }

    public BookingEntity toJpaEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .tripId(booking.getTripId())
                .customerId(booking.getCustomerId())
                .seatCount(booking.getSeatCount())
                .totalAmount(booking.getTotalAmount())
                .currency(booking.getCurrency())
                .status(booking.getStatus())
                .heldAt(booking.getHeldAt())
                .holdUntil(booking.getHoldUntil())
                .cancelledAt(booking.getCancelledAt())
                .note(booking.getNote())
                .creator(booking.getCreator())
                .build();
    }

    public BookingSeat toDomain(BookingSeatEntity entity) {
        return BookingSeat.builder()
                .id(entity.getId())
                .bookingId(entity.getBookingId())
                .tripId(entity.getTripId())
                .seatNo(entity.getSeatNo())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .creator(entity.getCreator())
                .build();
    }

    public BookingSeatEntity toJpaEntity(BookingSeat bookingSeat) {
        return BookingSeatEntity.builder()
                .id(bookingSeat.getId())
                .bookingId(bookingSeat.getBookingId())
                .tripId(bookingSeat.getTripId())
                .seatNo(bookingSeat.getSeatNo())
                .price(bookingSeat.getPrice())
                .status(bookingSeat.getStatus())
                .creator(bookingSeat.getCreator())
                .build();
    }
}
