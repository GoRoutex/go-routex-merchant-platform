package vn.com.routex.merchant.platform.domain.finance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RevenueTransaction extends AbstractAuditingEntity {
    private String id;
    private String ticketId;
    private String merchantId;
    private BigDecimal totalAmount;
    private BigDecimal commissionRate;
    private BigDecimal systemAmount;
    private BigDecimal merchantAmount;
    private OffsetDateTime transactionDate;
}
