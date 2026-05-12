package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.ticket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.ticket.entity.TicketEntity;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, String> {
    Optional<TicketEntity> findByTicketCode(String ticketCode);

    @Query("SELECT t FROM TicketEntity t WHERE " +
            "LOWER(t.ticketCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.customerName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.customerPhone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.customerEmail) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<TicketEntity> search(@Param("query") String query, Pageable pageable);

    boolean existsByTicketCode(String ticketCode);

    long countByMerchantId(String merchantId);

    Page<TicketEntity> findAllByMerchantId(String merchantId, Pageable pageable);
}


