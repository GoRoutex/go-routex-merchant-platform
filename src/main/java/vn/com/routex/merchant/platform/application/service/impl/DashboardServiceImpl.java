package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.dashboard.FetchMerchantDashboardQuery;
import vn.com.routex.merchant.platform.application.service.DashboardService;
import vn.com.routex.merchant.platform.domain.finance.port.RevenueRepositoryPort;
import vn.com.routex.merchant.platform.domain.route.port.RouteAggregateRepositoryPort;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;
import vn.com.routex.merchant.platform.domain.ticket.port.TicketRepositoryPort;
import vn.com.routex.merchant.platform.domain.trip.port.TripAggregateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.MerchantDailyStatsEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.repository.MerchantDailyStatsRepository;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.dashboard.response.MerchantDashboardResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TicketRepositoryPort ticketRepositoryPort;
    private final RevenueRepositoryPort revenueRepositoryPort;
    private final TripAggregateRepositoryPort tripRepositoryPort;
    private final RouteAggregateRepositoryPort routeRepositoryPort;
    private final MerchantDailyStatsRepository dailyStatsRepository;
    private final ApiResultFactory apiResultFactory;

    @Override
    public MerchantDashboardResponse getDashboard(FetchMerchantDashboardQuery query) {
        String merchantId = query.merchantId();
        String filterType = query.filterType();

        // Use the filter logic
        LocalDate now = LocalDate.now();
        LocalDate startDate = calculateStartDate(now, filterType);

        // Use Summary Tables for Performance
        List<MerchantDailyStatsEntity> dailyStats = dailyStatsRepository.findAllByMerchantIdAndStatsDateBetween(
                merchantId,
                startDate,
                now
        );

        // 1. Aggregate Statistics from Summary
        int totalTickets = dailyStats.stream().mapToInt(MerchantDailyStatsEntity::getTotalTickets).sum();
        BigDecimal totalRevenue = dailyStats.stream()
                .map(MerchantDailyStatsEntity::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal merchantShare = dailyStats.stream()
                .map(MerchantDailyStatsEntity::getMerchantShare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        MerchantDashboardResponse.Statistics stats = MerchantDashboardResponse.Statistics.builder()
                .ticketsSold(totalTickets)
                .totalRevenue(totalRevenue)
                .merchantShare(merchantShare)
                .ticketGrowthRate(12.5) // Mock value for now
                .revenueGrowthRate(8.2) // Mock value for now
                .build();

        // 2. Revenue Chart Data from Summary
        List<MerchantDashboardResponse.RevenueChartData> chartData = dailyStats.stream()
                .map(s -> MerchantDashboardResponse.RevenueChartData.builder()
                        .label(s.getStatsDate().format(DateTimeFormatter.ofPattern("dd/MM")))
                        .revenue(s.getTotalRevenue())
                        .date(s.getStatsDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset()))
                        .build())
                .sorted(Comparator.comparing(MerchantDashboardResponse.RevenueChartData::getDate))
                .collect(Collectors.toList());

        // 4. Popular Routes (Top 5)
        // Let's mock popular routes for now based on the image provided
        List<MerchantDashboardResponse.PopularRouteData> popularRoutes = List.of(
                MerchantDashboardResponse.PopularRouteData.builder().routeName("SÀI GÒN - ĐÀ LẠT").ticketCount(150).occupancyRate(92.0).build(),
                MerchantDashboardResponse.PopularRouteData.builder().routeName("SÀI GÒN - PHAN THIẾT").ticketCount(120).occupancyRate(85.0).build(),
                MerchantDashboardResponse.PopularRouteData.builder().routeName("SÀI GÒN - VŨNG TÀU").ticketCount(90).occupancyRate(78.0).build()
        );

        // 5. Recent Trips
        Page<Ticket> recentTicketPage = ticketRepositoryPort.findAllByMerchantId(merchantId, PageRequest.of(0, 5, Sort.by("createdAt").descending()));

        List<MerchantDashboardResponse.RecentTripData> recentTrips = recentTicketPage.getContent().stream()
                .map(t -> MerchantDashboardResponse.RecentTripData.builder()
                        .tripId(t.getTripId())
                        .routeName("Tuyến " + t.getTripId().substring(0, 5)) // Simplified
                        .vehiclePlate(t.getVehicleId())
                        .departureTime(t.getIssuedAt())
                        .status(t.getStatus().name())
                        .bookedSeats(1)
                        .build())
                .collect(Collectors.toList());

        return MerchantDashboardResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(MerchantDashboardResponse.DashboardData.builder()
                        .stats(stats)
                        .revenueChart(chartData)
                        .popularRoutes(popularRoutes)
                        .recentTrips(recentTrips)
                        .build())
                .build();
    }

    private LocalDate calculateStartDate(LocalDate now, String filterType) {
        if (filterType == null) return now.minusDays(7);

        return switch (filterType.toUpperCase()) {
            case "DAY" -> now;
            case "WEEK" -> now.minusWeeks(1);
            case "MONTH" -> now.minusMonths(1);
            case "YEAR" -> now.minusYears(1);
            default -> now.minusDays(7);
        };
    }
}

