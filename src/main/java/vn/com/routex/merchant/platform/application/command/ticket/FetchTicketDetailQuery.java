package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Builder
public record FetchTicketDetailQuery(
        BaseRequest context,
        String ticketId
) {}
