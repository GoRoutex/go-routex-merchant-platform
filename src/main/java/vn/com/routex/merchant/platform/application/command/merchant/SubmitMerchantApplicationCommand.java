package vn.com.routex.merchant.platform.application.command.merchant;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record SubmitMerchantApplicationCommand(
        RequestContext context,
        String displayName,
        String legalName,
        String taxCode,
        String businessLicense,
        String businessLicenseUrl,
        String country,
        String province,
        String district,
        String city,
        String postalCode,
        String description,
        String slug,
        Contact contact,
        BankInfo bankInfo,
        OwnerInfo ownerInfo
) {
    @Builder
    public record Contact(
            String contactName,
            String contactPhone,
            String contactEmail
    ) {
    }

    @Builder
    public record BankInfo(
            String bankName,
            String bankBranch,
            String bankAccountName,
            String bankAccountNumber
    ) {
    }

    @Builder
    public record OwnerInfo(
            String ownerName,
            String ownerFullName,
            String ownerPhone,
            String ownerEmail
    ) {
    }
}
