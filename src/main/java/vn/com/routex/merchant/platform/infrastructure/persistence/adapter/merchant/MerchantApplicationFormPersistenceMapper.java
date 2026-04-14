package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantApplicationFormEntity;

@Component
public class MerchantApplicationFormPersistenceMapper {

    public MerchantApplicationFormEntity toEntity(MerchantApplicationForm merchantApplicationForm) {
        MerchantApplicationFormEntity entity = new MerchantApplicationFormEntity();
        entity.setId(merchantApplicationForm.getId());
        entity.setDisplayName(merchantApplicationForm.getDisplayName());
        entity.setLegalName(merchantApplicationForm.getLegalName());
        entity.setContact(merchantApplicationForm.getContact());
        entity.setBankInfo(merchantApplicationForm.getBankInfo());
        entity.setOwnerInfo(merchantApplicationForm.getOwnerInfo());
        entity.setApprovedBy(merchantApplicationForm.getApprovedBy());
        entity.setApprovedAt(merchantApplicationForm.getApprovedAt());
        entity.setBusinessLicenseUrl(merchantApplicationForm.getBusinessLicenseUrl());
        entity.setLogoUrl(merchantApplicationForm.getLogoUrl());
        entity.setBusinessLicense(merchantApplicationForm.getBusinessLicense());
        entity.setDescription(merchantApplicationForm.getDescription());
        entity.setFormCode(merchantApplicationForm.getFormCode());
        entity.setPostalCode(merchantApplicationForm.getPostalCode());
        entity.setProvince(merchantApplicationForm.getProvince());
        entity.setCountry(merchantApplicationForm.getCountry());
        entity.setWard(merchantApplicationForm.getWard());
        entity.setAddress(merchantApplicationForm.getAddress());
        entity.setRejectedBy(merchantApplicationForm.getRejectedBy());
        entity.setRejectionReason(merchantApplicationForm.getRejectionReason());
        entity.setStatus(merchantApplicationForm.getStatus());
        entity.setSubmittedAt(merchantApplicationForm.getSubmittedAt());
        entity.setSubmittedBy(merchantApplicationForm.getSubmittedBy());
        entity.setTaxCode(merchantApplicationForm.getTaxCode());
        entity.setSlug(merchantApplicationForm.getSlug());
        entity.setCreatedAt(merchantApplicationForm.getCreatedAt());
        entity.setCreatedBy(merchantApplicationForm.getCreatedBy());
        entity.setUpdatedAt(merchantApplicationForm.getUpdatedAt());
        entity.setUpdatedBy(merchantApplicationForm.getUpdatedBy());
        return entity;
    }

    public MerchantApplicationForm toDomain(MerchantApplicationFormEntity entity) {
        return MerchantApplicationForm.builder()
                .id(entity.getId())
                .formCode(entity.getFormCode())
                .displayName(entity.getDisplayName())
                .legalName(entity.getLegalName())
                .contact(entity.getContact())
                .bankInfo(entity.getBankInfo())
                .ownerInfo(entity.getOwnerInfo())
                .approvedBy(entity.getApprovedBy())
                .approvedAt(entity.getApprovedAt())
                .businessLicenseUrl(entity.getBusinessLicenseUrl())
                .logoUrl(entity.getLogoUrl())
                .businessLicense(entity.getBusinessLicense())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .province(entity.getProvince())
                .description(entity.getDescription())
                .ward(entity.getWard())
                .address(entity.getAddress())
                .rejectedBy(entity.getRejectedBy())
                .rejectionReason(entity.getRejectionReason())
                .status(entity.getStatus())
                .submittedAt(entity.getSubmittedAt())
                .submittedBy(entity.getSubmittedBy())
                .taxCode(entity.getTaxCode())
                .slug(entity.getSlug())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
