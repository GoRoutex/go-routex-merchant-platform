package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.ticket.TicketStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Builder
public record UpdateTicketCommand(
        BaseRequest context,
        String ticketId,
        String customerName,
        String customerPhone,
        String customerEmail,
        TicketStatus status
) {}
