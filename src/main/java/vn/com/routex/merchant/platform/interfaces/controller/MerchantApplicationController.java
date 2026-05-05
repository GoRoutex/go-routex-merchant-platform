package vn.com.routex.merchant.platform.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.service.MerchantApplicationFormService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.model.merchant.AcceptMerchantApplicationRequest;
import vn.com.routex.merchant.platform.interfaces.model.merchant.AcceptMerchantApplicationResponse;
import vn.com.routex.merchant.platform.interfaces.model.merchant.RejectMerchantApplicationRequest;
import vn.com.routex.merchant.platform.interfaces.model.merchant.RejectMerchantApplicationResponse;
import vn.com.routex.merchant.platform.interfaces.model.merchant.SubmitMerchantApplicationRequest;
import vn.com.routex.merchant.platform.interfaces.model.merchant.SubmitMerchantApplicationResponse;

import java.math.BigDecimal;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.ACCEPT_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.ADMIN_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.APPLICATIONS_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.REJECT_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.SUBMIT_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
public class MerchantApplicationController {

    private final MerchantApplicationFormService merchantApplicationFormService;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }
    /*
    * Submit application form for Merchant Registration
    * */
    @PostMapping(APPLICATIONS_PATH + SUBMIT_PATH)
    public ResponseEntity<SubmitMerchantApplicationResponse> submitApplication(
            @Valid @RequestBody SubmitMerchantApplicationRequest request
    ) {

        sLog.info("[SUBMIT-FORM] Merchant application form request: {}", request);
        SubmitMerchantApplicationResult result = merchantApplicationFormService.submit(toCommand(request));

        SubmitMerchantApplicationResponse response = SubmitMerchantApplicationResponse.builder()
                .result(ExceptionUtils.buildResultResponse(SUCCESS_CODE, SUCCESS_MESSAGE))
                .data(SubmitMerchantApplicationResponse.SubmitMerchantApplicationResponseData.builder()
                        .applicationId(result.applicationId())
                        .formCode(result.formCode())
                        .displayName(result.displayName())
                        .legalName(result.legalName())
                        .status(result.status())
                        .submittedAt(result.submittedAt())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(ADMIN_PATH + APPLICATIONS_PATH + ACCEPT_PATH)
    public ResponseEntity<AcceptMerchantApplicationResponse> acceptApplication(
            @Valid @RequestBody AcceptMerchantApplicationRequest request
    ) {
        AcceptMerchantApplicationResult result = merchantApplicationFormService.accept(toCommand(request));

        AcceptMerchantApplicationResponse response = AcceptMerchantApplicationResponse.builder()
                .result(ExceptionUtils.buildResultResponse(SUCCESS_CODE, SUCCESS_MESSAGE))
                .data(AcceptMerchantApplicationResponse.AcceptMerchantApplicationResponseData.builder()
                        .applicationId(result.applicationId())
                        .formCode(result.formCode())
                        .merchantId(result.merchantId())
                        .merchantCode(result.merchantCode())
                        .merchantName(result.merchantName())
                        .status(result.status())
                        .approvedBy(result.approvedBy())
                        .approvedAt(result.approvedAt())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(ADMIN_PATH + APPLICATIONS_PATH + REJECT_PATH)
    public ResponseEntity<RejectMerchantApplicationResponse> rejectApplication(
            @Valid @RequestBody RejectMerchantApplicationRequest request
    ) {
        RejectMerchantApplicationResult result = merchantApplicationFormService.reject(toCommand(request));

        RejectMerchantApplicationResponse response = RejectMerchantApplicationResponse.builder()
                .result(ExceptionUtils.buildResultResponse(SUCCESS_CODE, SUCCESS_MESSAGE))
                .data(RejectMerchantApplicationResponse.RejectMerchantApplicationResponseData.builder()
                        .applicationId(result.applicationId())
                        .formCode(result.formCode())
                        .status(result.status())
                        .rejectedBy(result.rejectedBy())
                        .rejectionReason(result.rejectionReason())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    private SubmitMerchantApplicationCommand toCommand(SubmitMerchantApplicationRequest request) {
        SubmitMerchantApplicationRequest.SubmitMerchantApplicationRequestData data = request.getData();
        return SubmitMerchantApplicationCommand.builder()
                .context(HttpUtils.toContext(request))
                .displayName(data.getDisplayName())
                .legalName(data.getLegalName())
                .taxCode(data.getTaxCode())
                .businessLicense(data.getBusinessLicense())
                .businessLicenseUrl(data.getBusinessLicenseUrl())
                .logoUrl(data.getLogoUrl())
                .address(SubmitMerchantApplicationCommand.Address.builder()
                        .country(data.getAddressInfo().getCountry())
                        .province(data.getAddressInfo().getProvince())
                        .city(data.getAddressInfo().getCity())
                        .postalCode(data.getAddressInfo().getPostalCode())
                        .ward(data.getAddressInfo().getWard())
                        .address(data.getAddressInfo().getAddress())
                        .build()
                )
                .description(data.getDescription())
                .slug(data.getSlug())
                .contact(SubmitMerchantApplicationCommand.Contact.builder()
                        .contactName(data.getContact().getContactName())
                        .contactPhone(data.getContact().getContactPhone())
                        .contactEmail(data.getContact().getContactEmail())
                        .build())
                .bankInfo(SubmitMerchantApplicationCommand.BankInfo.builder()
                        .bankName(data.getBankInfo().getBankName())
                        .bankBranch(data.getBankInfo().getBankBranch())
                        .bankAccountName(data.getBankInfo().getBankAccountName())
                        .bankAccountNumber(data.getBankInfo().getBankAccountNumber())
                        .build())
                .ownerInfo(SubmitMerchantApplicationCommand.OwnerInfo.builder()
                        .ownerName(data.getOwnerInfo().getOwnerName())
                        .ownerFullName(data.getOwnerInfo().getOwnerFullName())
                        .ownerPhone(data.getOwnerInfo().getOwnerPhone())
                        .ownerEmail(data.getOwnerInfo().getOwnerEmail())
                        .build())
                .build();
    }

    private AcceptMerchantApplicationCommand toCommand(AcceptMerchantApplicationRequest request) {
        return AcceptMerchantApplicationCommand.builder()
                .context(HttpUtils.toContext(request))
                .applicationFormId(request.getData().getApplicationFormId())
                .approvedBy(request.getData().getApprovedBy())
                .commission(new BigDecimal(request.getData().getCommission()))
                .build();
    }

    private RejectMerchantApplicationCommand toCommand(RejectMerchantApplicationRequest request) {
        return RejectMerchantApplicationCommand.builder()
                .context(HttpUtils.toContext(request))
                .applicationFormId(request.getData().getApplicationFormId())
                .rejectedBy(request.getData().getRejectedBy())
                .rejectionReason(request.getData().getRejectionReason())
                .build();
    }

}
