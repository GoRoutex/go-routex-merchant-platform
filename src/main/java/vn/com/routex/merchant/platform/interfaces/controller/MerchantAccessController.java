package vn.com.routex.merchant.platform.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantCommand;
import vn.com.routex.merchant.platform.application.command.merchant.GetMyMerchantResult;
import vn.com.routex.merchant.platform.application.service.MerchantAccessService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.merchant.GetMyMerchantResponse;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.PROFILE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
public class MerchantAccessController {

    private final ApiResultFactory apiResultFactory;
    private final MerchantAccessService merchantAccessService;

    @GetMapping(PROFILE_PATH)
    public ResponseEntity<GetMyMerchantResponse> getMyMerchantInfo(
            HttpServletRequest servletRequest,
            @RequestParam String merchantId) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        GetMyMerchantResult result = merchantAccessService.fetchMerchantDetail(
                GetMyMerchantCommand.builder()
                        .context(HttpUtils.toContext(baseRequest))
                        .merchantId(merchantId)
                        .build()
        );

        GetMyMerchantResponse response = GetMyMerchantResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(GetMyMerchantResponse.GetMyMerchantResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .slug(result.slug())
                        .displayName(result.displayName())
                        .legalName(result.legalName())
                        .taxCode(result.taxCode())
                        .businessLicenseNumber(result.businessLicenseNumber())
                        .businessLicenseUrl(result.businessLicenseUrl())
                        .phone(result.phone())
                        .email(result.email())
                        .logoUrl(result.logoUrl())
                        .description(result.description())
                        .address(result.address())
                        .ward(result.ward())
                        .province(result.province())
                        .country(result.country())
                        .postalCode(result.postalCode())
                        .representativeName(result.representativeName())
                        .contactName(result.contactName())
                        .contactPhone(result.contactPhone())
                        .contactEmail(result.contactEmail())
                        .ownerFullName(result.ownerFullName())
                        .ownerPhone(result.ownerPhone())
                        .ownerEmail(result.ownerEmail())
                        .bankAccountName(result.bankAccountName())
                        .bankAccountNumber(result.bankAccountNumber())
                        .bankName(result.bankName())
                        .bankBranch(result.bankBranch())
                        .commissionRate(result.commissionRate())
                        .status(result.status())
                        .approvedAt(result.approvedAt())
                        .approvedBy(result.approvedBy())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

}
