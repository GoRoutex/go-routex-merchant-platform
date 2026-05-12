package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.ticket.TicketStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Builder
public record FetchTicketListQuery(
        BaseRequest context,
        String query,
        TicketStatus status,
        int pageNumber,
        int pageSize
) {}
