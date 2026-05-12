package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.merchant.platform.application.command.ticket.CreateTicketCommand;
import vn.com.routex.merchant.platform.application.service.FinanceSummaryService;
import vn.com.routex.merchant.platform.application.service.TicketService;
import vn.com.routex.merchant.platform.domain.finance.model.RevenueTransaction;
import vn.com.routex.merchant.platform.domain.finance.port.RevenueRepositoryPort;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.merchant.platform.domain.ticket.TicketStatus;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;
import vn.com.routex.merchant.platform.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.merchant.platform.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepositoryPort ticketRepositoryPort;
    private final MerchantRepositoryPort merchantRepositoryPort;
    private final RevenueRepositoryPort revenueRepositoryPort;
    private final FinanceSummaryService financeSummaryService;
    private final TripAggregateRepositoryPort tripRepositoryPort;

    @Override
    @Transactional
    public List<Ticket> createTickets(List<CreateTicketCommand> commands) {
        if (commands.isEmpty()) return List.of();

        // Group by merchantId to minimize repository calls
        Map<String, Merchant> merchantCache = commands.stream()
                .map(CreateTicketCommand::merchantId)
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> merchantRepositoryPort.findById(id)
                                .orElseThrow(() -> new BusinessException(ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Merchant not found: " + id)))
                ));

        return commands.stream().map(command -> {
            Merchant merchant = merchantCache.get(command.merchantId());
            BigDecimal commissionRate = merchant.getCommissionRate() != null ? merchant.getCommissionRate() : BigDecimal.ZERO;

            Ticket ticket = Ticket.builder()
                    .id(UUID.randomUUID().toString())
                    .ticketCode(ticketRepositoryPort.generateTicketCode())
                    .bookingId(command.bookingId())
                    .bookingSeatId(command.bookingSeatId())
                    .merchantId(command.merchantId())
                    .tripId(command.tripId())
                    .vehicleId(command.vehicleId())
                    .seatNumber(command.seatNumber())
                    .customerName(command.customerName())
                    .customerPhone(command.customerPhone())
                    .customerEmail(command.customerEmail())
                    .price(command.price())
                    .status(TicketStatus.ISSUED)
                    .issuedAt(command.issuedAt() != null ? command.issuedAt() : OffsetDateTime.now())
                    .createdBy(command.creator())
                    .createdAt(OffsetDateTime.now())
                    .build();

            Ticket savedTicket = ticketRepositoryPort.save(ticket);

            // Calculate Revenue Split
            BigDecimal totalAmount = command.price();
            BigDecimal systemAmount = totalAmount.multiply(commissionRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal merchantAmount = totalAmount.subtract(systemAmount);

            RevenueTransaction revenueTransaction = RevenueTransaction.builder()
                    .id(UUID.randomUUID().toString())
                    .ticketId(savedTicket.getId())
                    .merchantId(merchant.getId())
                    .totalAmount(totalAmount)
                    .commissionRate(commissionRate)
                    .systemAmount(systemAmount)
                    .merchantAmount(merchantAmount)
                    .transactionDate(OffsetDateTime.now())
                    .createdAt(OffsetDateTime.now())
                    .createdBy(command.creator())
                    .build();

            revenueRepositoryPort.save(revenueTransaction);

            // Update Summaries for Performance and AI Training
            financeSummaryService.updateDailyStats(merchant.getId(), revenueTransaction.getTransactionDate(), totalAmount, merchantAmount, systemAmount, 1);
            
            // For AI Training data (Trip Demand)
            tripRepositoryPort.findById(command.tripId()).ifPresent(trip -> {
                financeSummaryService.updateTripDemand(
                    trip.getId(),
                    merchant.getId(),
                    trip.getRouteId(),
                    trip.getDepartureTime(),
                    45, // Default capacity for now (e.g., Sleeper bus)
                    1   // Initial booked seat if not tracked
                );
            });


            return savedTicket;
        }).collect(Collectors.toList());
    }





    @Override
    @Transactional
    public Ticket updateTicket(String id, Ticket ticketUpdate) {
        Ticket existingTicket = ticketRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Only update certain fields as per merchant management needs
        existingTicket.setCustomerName(ticketUpdate.getCustomerName());
        existingTicket.setCustomerPhone(ticketUpdate.getCustomerPhone());
        existingTicket.setCustomerEmail(ticketUpdate.getCustomerEmail());
        existingTicket.setStatus(ticketUpdate.getStatus());
        
        if (ticketUpdate.getCheckedInAt() != null) existingTicket.setCheckedInAt(ticketUpdate.getCheckedInAt());
        if (ticketUpdate.getBoardedAt() != null) existingTicket.setBoardedAt(ticketUpdate.getBoardedAt());
        if (ticketUpdate.getCancelledAt() != null) existingTicket.setCancelledAt(ticketUpdate.getCancelledAt());
        
        return ticketRepositoryPort.save(existingTicket);
    }

    @Override
    public Ticket getTicketDetail(String id) {
        return ticketRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    @Override
    public Page<Ticket> getTickets(String query, Pageable pageable) {
        if (query != null && !query.isEmpty()) {
            return ticketRepositoryPort.search(query, pageable);
        }
        return ticketRepositoryPort.findAll(pageable);
    }
}
