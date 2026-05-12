package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.merchant.platform.application.service.FinanceSummaryService;
import vn.com.routex.merchant.platform.application.service.HolidayService;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.MerchantDailyStatsEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.TripDemandHistoryEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.repository.MerchantDailyStatsRepository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.repository.TripDemandHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class FinanceSummaryServiceImpl implements FinanceSummaryService {

    private final MerchantDailyStatsRepository dailyStatsRepository;
    private final TripDemandHistoryRepository demandHistoryRepository;
    private final HolidayService holidayService;

    @Override
    @Transactional
    public void updateDailyStats(String merchantId, OffsetDateTime date, BigDecimal amount, BigDecimal merchantShare, BigDecimal systemCommission, int ticketCount) {
        LocalDate statsDate = date.toLocalDate();
        String id = merchantId + "_" + statsDate;

        MerchantDailyStatsEntity entity = dailyStatsRepository.findById(id)
                .orElse(MerchantDailyStatsEntity.builder()
                        .id(id)
                        .merchantId(merchantId)
                        .statsDate(statsDate)
                        .totalTickets(0)
                        .totalRevenue(BigDecimal.ZERO)
                        .merchantShare(BigDecimal.ZERO)
                        .systemCommission(BigDecimal.ZERO)
                        .build());

        entity.setTotalTickets(entity.getTotalTickets() + ticketCount);
        entity.setTotalRevenue(entity.getTotalRevenue().add(amount));
        entity.setMerchantShare(entity.getMerchantShare().add(merchantShare));
        entity.setSystemCommission(entity.getSystemCommission().add(systemCommission));

        dailyStatsRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateTripDemand(String tripId, String merchantId, String routeId, OffsetDateTime departureTime, int totalSeats, int bookedSeats) {
        TripDemandHistoryEntity entity = demandHistoryRepository.findById(tripId)
                .orElse(TripDemandHistoryEntity.builder()
                        .id(tripId)
                        .merchantId(merchantId)
                        .routeId(routeId)
                        .departureDate(departureTime.toLocalDate())
                        .departureHour(departureTime.getHour())
                        .dayOfWeek(departureTime.getDayOfWeek().getValue())
                        .totalSeats(totalSeats)
                        .bookedSeats(0)
                        .isHoliday(holidayService.isHolidayOrPeakDay(departureTime.toLocalDate()))
                        .build());

        entity.setBookedSeats(bookedSeats);
        entity.setOccupancyRate((double) entity.getBookedSeats() / entity.getTotalSeats() * 100);

        demandHistoryRepository.save(entity);
    }
}
