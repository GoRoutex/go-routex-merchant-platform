package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.ticket.TicketStatus;

@Builder
public record CreateTicketResult(
        String ticketId,
        String ticketCode,
        TicketStatus status
) {}
