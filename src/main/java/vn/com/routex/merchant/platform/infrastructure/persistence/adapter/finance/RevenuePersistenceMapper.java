package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.finance;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.finance.model.RevenueTransaction;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.RevenueTransactionEntity;

@Component
public class RevenuePersistenceMapper {

    public RevenueTransaction toDomain(RevenueTransactionEntity entity) {
        if (entity == null) return null;
        return RevenueTransaction.builder()
                .id(entity.getId())
                .ticketId(entity.getTicketId())
                .merchantId(entity.getMerchantId())
                .totalAmount(entity.getTotalAmount())
                .commissionRate(entity.getCommissionRate())
                .systemAmount(entity.getSystemAmount())
                .merchantAmount(entity.getMerchantAmount())
                .transactionDate(entity.getTransactionDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public RevenueTransactionEntity toEntity(RevenueTransaction domain) {
        if (domain == null) return null;
        return RevenueTransactionEntity.builder()
                .id(domain.getId())
                .ticketId(domain.getTicketId())
                .merchantId(domain.getMerchantId())
                .totalAmount(domain.getTotalAmount())
                .commissionRate(domain.getCommissionRate())
                .systemAmount(domain.getSystemAmount())
                .merchantAmount(domain.getMerchantAmount())
                .transactionDate(domain.getTransactionDate())
                .createdAt(domain.getCreatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedAt(domain.getUpdatedAt())
                .updatedBy(domain.getUpdatedBy())
                .build();
    }
}
