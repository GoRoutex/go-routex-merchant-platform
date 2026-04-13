package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormContact;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormOwner;
import vn.com.routex.merchant.platform.domain.merchant.MerchantStatus;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "MERCHANTS")
public class MerchantEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "TAX_CODE")
    private String taxCode;

    @Lob
    @Column(name = "LOGO_URL")
    private String logoUrl;

    @Column(name = "BUSIENSS_LICENSE_NUMBER")
    private String businessLicenseNumber;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "REPRESENTATIVE_NAME")
    private String representativeName;

    @Embedded
    private ApplicationFormBankInfo bankInfo;
    @Embedded
    private ApplicationFormContact contact;
    @Embedded
    private ApplicationFormOwner owner;

    @Column(name = "COMMISSION_RATE", precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;



}
