package vn.com.routex.merchant.platform.domain.merchant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormContact;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormOwner;
import vn.com.routex.merchant.platform.domain.merchant.MerchantStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Merchant extends AbstractAuditingEntity {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private String id;
    private String code;
    private String name;
    private String taxCode;
    private String phone;
    private String email;
    private String logoUrl;
    private String businessLicenseNumber;
    private ApplicationFormContact contact;
    private ApplicationFormBankInfo bankInfo;
    private ApplicationFormOwner ownerInfo;
    private String address;
    private String representativeName;
    private BigDecimal commissionRate;
    private MerchantStatus status;

    public void updateCommissionRate(BigDecimal commissionRate, String actor, OffsetDateTime updatedAt) {
        this.commissionRate = commissionRate;
        this.setUpdatedBy(actor);
        this.setUpdatedAt(updatedAt);
    }

    public BigDecimal calculatePlatformCommissionAmount(BigDecimal ticketPrice) {
        validateTicketPrice(ticketPrice);
        return ticketPrice
                .multiply(getCommissionRateFraction())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMerchantProfitAmount(BigDecimal ticketPrice) {
        validateTicketPrice(ticketPrice);
        return ticketPrice
                .subtract(calculatePlatformCommissionAmount(ticketPrice))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateMerchantProfitRate() {
        return ONE_HUNDRED
                .subtract(normalizeCommissionRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getCommissionRateFraction() {
        return normalizeCommissionRate()
                .divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal normalizeCommissionRate() {
        return commissionRate == null ? BigDecimal.ZERO : commissionRate;
    }

    private void validateTicketPrice(BigDecimal ticketPrice) {
        if (ticketPrice == null || ticketPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("ticketPrice must be non-negative");
        }
    }
}
