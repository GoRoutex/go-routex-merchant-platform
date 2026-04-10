package vn.com.routex.merchant.platform.domain.merchant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.merchant.platform.domain.merchant.MerchantStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Merchant extends AbstractAuditingEntity {

    private String id;
    private String code;
    private String name;
    private String taxCode;
    private String phone;
    private String email;
    private String address;
    private String representativeName;
    private BigDecimal commissionRate;
    private MerchantStatus status;

    public static Merchant create(
            String id,
            String code,
            String name,
            String taxCode,
            String phone,
            String email,
            String address,
            String representativeName,
            BigDecimal commissionRate,
            String createdBy
    ) {
        OffsetDateTime now = OffsetDateTime.now();
        return Merchant.builder()
                .id(id)
                .code(code)
                .name(name)
                .taxCode(taxCode)
                .phone(phone)
                .email(email)
                .address(address)
                .representativeName(representativeName)
                .commissionRate(commissionRate)
                .status(MerchantStatus.ACTIVE)
                .createdAt(now)
                .createdBy(createdBy)
                .build();
    }

    public void updateCommissionRate(BigDecimal commissionRate, String actor, OffsetDateTime updatedAt) {
        this.commissionRate = commissionRate;
        this.setUpdatedBy(actor);
        this.setUpdatedAt(updatedAt);
    }
}
