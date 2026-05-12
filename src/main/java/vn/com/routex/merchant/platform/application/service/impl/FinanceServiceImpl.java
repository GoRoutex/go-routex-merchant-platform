package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.finance.FetchMerchantRevenueQuery;
import vn.com.routex.merchant.platform.application.command.finance.FetchSystemRevenueQuery;
import vn.com.routex.merchant.platform.application.service.FinanceService;
import vn.com.routex.merchant.platform.domain.finance.model.RevenueTransaction;
import vn.com.routex.merchant.platform.domain.finance.port.RevenueRepositoryPort;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.MerchantRevenueResponse;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.SystemRevenueResponse;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final RevenueRepositoryPort revenueRepositoryPort;
    private final ApiResultFactory apiResultFactory;

    @Override
    public MerchantRevenueResponse getMerchantRevenue(FetchMerchantRevenueQuery query) {
        List<RevenueTransaction> transactions = revenueRepositoryPort.findAllByMerchantId(query.merchantId());
        
        // Filter by date range if provided
        List<RevenueTransaction> filtered = transactions.stream()
                .filter(t -> (query.startDate() == null || t.getTransactionDate().isAfter(query.startDate())) &&
                             (query.endDate() == null || t.getTransactionDate().isBefore(query.endDate())))
                .toList();

        BigDecimal totalRevenue = filtered.stream().map(RevenueTransaction::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal merchantShare = filtered.stream().map(RevenueTransaction::getMerchantAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal systemCommission = filtered.stream().map(RevenueTransaction::getSystemAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return MerchantRevenueResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(MerchantRevenueResponse.MerchantRevenueData.builder()
                        .merchantId(query.merchantId())
                        .totalRevenue(totalRevenue)
                        .merchantShare(merchantShare)
                        .systemCommission(systemCommission)
                        .ticketCount(filtered.size())
                        .build())
                .build();
    }

    @Override
    public SystemRevenueResponse getSystemRevenue(FetchSystemRevenueQuery query) {
        BigDecimal totalSystemCommission = revenueRepositoryPort.sumSystemAmount(query.startDate(), query.endDate());
        
        return SystemRevenueResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(SystemRevenueResponse.SystemRevenueData.builder()
                        .totalSystemCommission(totalSystemCommission != null ? totalSystemCommission : BigDecimal.ZERO)
                        .build())
                .build();
    }
}

