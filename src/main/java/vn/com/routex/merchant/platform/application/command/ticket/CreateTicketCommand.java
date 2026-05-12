package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record CreateTicketCommand(
        BaseRequest context,
        String bookingId,
        String bookingSeatId,
        String merchantId,
        String tripId,
        String vehicleId,
        String seatNumber,
        String customerName,
        String customerPhone,
        String customerEmail,
        BigDecimal price,
        OffsetDateTime issuedAt,
        String creator
) {}
