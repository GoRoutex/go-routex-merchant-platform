package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantCommand;
import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantResult;
import vn.com.routex.merchant.platform.application.service.MerchantAccessService;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.MERCHANT_NOT_FOUND_BY_ID;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class MerchantAccessServiceImpl implements MerchantAccessService {

    private final MerchantRepositoryPort merchantRepositoryPort;

    @Override
    public GetMyMerchantResult fetchMerchantDetail(GetMyMerchantCommand query) {
        Merchant merchant = merchantRepositoryPort.findById(query.merchantId())
                .orElseThrow(() -> new BusinessException(
                        query.context().requestId(),
                        query.context().requestDateTime(),
                        query.context().channel(),
                        ExceptionUtils.buildResultResponse(
                                RECORD_NOT_FOUND,
                                String.format(MERCHANT_NOT_FOUND_BY_ID, query.merchantId())
                        )
                ));

        return GetMyMerchantResult.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .slug(merchant.getSlug())
                .displayName(merchant.getDisplayName())
                .legalName(merchant.getLegalName())
                .taxCode(merchant.getTaxCode())
                .businessLicenseNumber(merchant.getBusinessLicenseNumber())
                .businessLicenseUrl(merchant.getBusinessLicenseUrl())
                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .logoUrl(merchant.getLogoUrl())
                .description(merchant.getDescription())
                .address(merchant.getAddress())
                .ward(merchant.getWard())
                .province(merchant.getProvince())
                .country(merchant.getCountry())
                .postalCode(merchant.getPostalCode())
                .representativeName(merchant.getRepresentativeName())
                .contactName(merchant.getContactName())
                .contactPhone(merchant.getContactPhone())
                .contactEmail(merchant.getContactEmail())
                .ownerFullName(merchant.getOwnerFullName())
                .ownerPhone(merchant.getOwnerPhone())
                .ownerEmail(merchant.getOwnerEmail())
                .bankAccountName(merchant.getBankAccountName())
                .bankAccountNumber(merchant.getBankAccountNumber())
                .bankName(merchant.getBankName())
                .bankBranch(merchant.getBankBranch())
                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())
                .approvedAt(merchant.getApprovedAt())
                .approvedBy(merchant.getApprovedBy())
                .build();
    }
}
