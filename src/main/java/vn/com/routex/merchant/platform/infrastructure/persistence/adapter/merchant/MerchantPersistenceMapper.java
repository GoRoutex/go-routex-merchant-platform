package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;


@Component
public class MerchantPersistenceMapper {


    public MerchantEntity toEntity(Merchant merchant) {
        return MerchantEntity.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .name(merchant.getName())
                .taxCode(merchant.getTaxCode())
                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .logoUrl(merchant.getLogoUrl())
                .businessLicenseNumber(merchant.getBusinessLicenseNumber())
                .address(merchant.getAddress())
                .representativeName(merchant.getRepresentativeName())
                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())
                .contact(merchant.getContact())
                .bankInfo(merchant.getBankInfo())
                .owner(merchant.getOwnerInfo())
                .createdAt(merchant.getCreatedAt())
                .createdBy(merchant.getCreatedBy())
                .updatedAt(merchant.getUpdatedAt())
                .updatedBy(merchant.getUpdatedBy())
                .build();
    }

    public Merchant toDomain(MerchantEntity entity) {
        return Merchant.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .taxCode(entity.getTaxCode())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .logoUrl(entity.getLogoUrl())
                .businessLicenseNumber(entity.getBusinessLicenseNumber())
                .address(entity.getAddress())
                .representativeName(entity.getRepresentativeName())
                .commissionRate(entity.getCommissionRate())
                .status(entity.getStatus())
                .contact(entity.getContact())
                .bankInfo(entity.getBankInfo())
                .ownerInfo(entity.getOwner())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

}
