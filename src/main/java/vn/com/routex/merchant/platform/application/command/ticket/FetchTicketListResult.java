package vn.com.routex.merchant.platform.application.command.ticket;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;

import java.util.List;

@Builder
public record FetchTicketListResult(
        List<Ticket> items,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {}
