package vn.com.routex.merchant.platform.application.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface FinanceSummaryService {
    void updateDailyStats(String merchantId, OffsetDateTime date, BigDecimal amount, BigDecimal merchantShare, BigDecimal systemCommission, int ticketCount);
    void updateTripDemand(String tripId, String merchantId, String routeId, OffsetDateTime departureTime, int totalSeats, int bookedSeats);
}
