package vn.com.routex.merchant.platform.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.routex.merchant.platform.application.command.ticket.CreateTicketCommand;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> createTickets(List<CreateTicketCommand> commands);
    Ticket updateTicket(String id, Ticket ticketUpdate);
    Ticket getTicketDetail(String id);
    Page<Ticket> getTickets(String query, Pageable pageable);
}

