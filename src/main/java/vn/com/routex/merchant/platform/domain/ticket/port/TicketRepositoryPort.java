package vn.com.routex.merchant.platform.domain.ticket.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;

import java.util.Optional;

public interface TicketRepositoryPort {
    Ticket save(Ticket ticket);
    Optional<Ticket> findById(String id);
    Optional<Ticket> findByTicketCode(String ticketCode);
    Page<Ticket> findAll(Pageable pageable);
    Page<Ticket> search(String query, Pageable pageable);
    String generateTicketCode();
    long countByMerchantId(String merchantId);
    Page<Ticket> findAllByMerchantId(String merchantId, Pageable pageable);
}

