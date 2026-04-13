package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.service.MerchantApplicationFormService;
import vn.com.routex.merchant.platform.domain.authorities.model.RoleAggregate;
import vn.com.routex.merchant.platform.domain.authorities.model.UserAccountReference;
import vn.com.routex.merchant.platform.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.merchant.platform.domain.authorities.port.RoleRepositoryPort;
import vn.com.routex.merchant.platform.domain.authorities.port.UserAccountLookupPort;
import vn.com.routex.merchant.platform.domain.authorities.port.UserRoleAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormBankInfo;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormContact;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormOwner;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormStatus;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantApplicationFormRepositoryPort;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_MERCHANT_COMMISSION_RATE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantApplicationFormServiceImpl implements MerchantApplicationFormService {

    private static final String MERCHANT_OWNER_ROLE_CODE = "MERCHANT_OWNER";

    private final MerchantApplicationFormRepositoryPort merchantApplicationFormRepositoryPort;
    private final MerchantRepositoryPort merchantRepositoryPort;
    private final UserAccountLookupPort userAccountLookupPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRoleAssignmentRepositoryPort userRoleAssignmentRepositoryPort;

    @Override
    @Transactional
    public SubmitMerchantApplicationResult submit(SubmitMerchantApplicationCommand command) {
        MerchantApplicationForm applicationForm = MerchantApplicationForm.submit(
                UUID.randomUUID().toString(),
                merchantApplicationFormRepositoryPort.generateFormCode(),
                command.displayName(),
                command.legalName(),
                command.taxCode(),
                command.businessLicense(),
                command.businessLicenseUrl(),
                command.address().country(),
                command.address().province(),
                command.address().ward(),
                command.address().address(),
                command.address().postalCode(),
                command.description(),
                resolveSlug(command),
                command.contact().contactName(),
                command.contact().contactPhone(),
                command.contact().contactEmail(),
                command.bankInfo().bankName(),
                command.bankInfo().bankBranch(),
                command.bankInfo().bankAccountName(),
                command.bankInfo().bankAccountNumber(),
                command.ownerInfo().ownerName(),
                command.ownerInfo().ownerFullName(),
                command.ownerInfo().ownerPhone(),
                command.ownerInfo().ownerEmail()
        );

        MerchantApplicationForm savedApplicationForm = merchantApplicationFormRepositoryPort.save(applicationForm);

        return SubmitMerchantApplicationResult.builder()
                .applicationId(savedApplicationForm.getId())
                .formCode(savedApplicationForm.getFormCode())
                .displayName(savedApplicationForm.getDisplayName())
                .legalName(savedApplicationForm.getLegalName())
                .status(savedApplicationForm.getStatus().name())
                .submittedAt(savedApplicationForm.getSubmittedAt())
                .build();
    }

    @Override
    @Transactional
    public AcceptMerchantApplicationResult accept(AcceptMerchantApplicationCommand command) {
        MerchantApplicationForm applicationForm = merchantApplicationFormRepositoryPort.findById(command.applicationFormId())
                .orElseThrow(() -> notFound(command.context()));

        validatePendingApplication(applicationForm, command.context());

        Merchant merchant = Merchant.builder()
                .id(UUID.randomUUID().toString())
                .code(merchantRepositoryPort.generateMerchantCode())
                .name(applicationForm.getDisplayName())
                .taxCode(applicationForm.getTaxCode())
                .logoUrl(applicationForm.getLogoUrl())
                .businessLicenseNumber(applicationForm.getBusinessLicense())
                .contact(ApplicationFormContact.builder()
                        .contactEmail(applicationForm.getContact().getContactEmail())
                        .contactPhone(applicationForm.getContact().getContactPhone())
                        .contactName(applicationForm.getContact().getContactName())
                        .build())
                .bankInfo(ApplicationFormBankInfo.builder()
                        .bankAccountName(applicationForm.getBankInfo().getBankAccountName())
                        .bankAccountNumber(applicationForm.getBankInfo().getBankAccountNumber())
                        .bankBranch(applicationForm.getBankInfo().getBankBranch())
                        .bankName(applicationForm.getBankInfo().getBankName())
                        .build())
                .ownerInfo(ApplicationFormOwner.builder()
                        .ownerPhone(applicationForm.getOwnerInfo().getOwnerPhone())
                        .ownerEmail(applicationForm.getOwnerInfo().getOwnerEmail())
                        .ownerFullName(applicationForm.getOwnerInfo().getOwnerFullName())
                        .ownerName(applicationForm.getOwnerInfo().getOwnerName())
                        .build())
                .address(applicationForm.getAddress())
                .representativeName(applicationForm.getOwnerInfo().getOwnerFullName())
                .commissionRate(command.commission() != null ? command.commission() : DEFAULT_MERCHANT_COMMISSION_RATE)
                .build();

        Merchant savedMerchant = merchantRepositoryPort.save(merchant);

        assignMerchantOwnerRole(
                applicationForm.getSubmittedBy(),
                command.context()
        );

        applicationForm.approve(savedMerchant.getId(), command.approvedBy(), OffsetDateTime.now());
        MerchantApplicationForm savedApplicationForm = merchantApplicationFormRepositoryPort.save(applicationForm);

        return AcceptMerchantApplicationResult.builder()
                .applicationId(savedApplicationForm.getId())
                .formCode(savedApplicationForm.getFormCode())
                .merchantId(savedMerchant.getId())
                .merchantCode(savedMerchant.getCode())
                .merchantName(savedMerchant.getName())
                .status(savedApplicationForm.getStatus().name())
                .approvedBy(savedApplicationForm.getApprovedBy())
                .approvedAt(savedApplicationForm.getApprovedAt())
                .build();
    }

    @Override
    @Transactional
    public RejectMerchantApplicationResult reject(RejectMerchantApplicationCommand command) {
        MerchantApplicationForm applicationForm = merchantApplicationFormRepositoryPort.findById(command.applicationFormId())
                .orElseThrow(() -> notFound(command.context()));

        validatePendingApplication(applicationForm, command.context());

        if (command.rejectionReason() == null || command.rejectionReason().isBlank()) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "rejectionReason is required")
            );
        }

        applicationForm.reject(command.rejectedBy(), command.rejectionReason().trim(), OffsetDateTime.now());
        MerchantApplicationForm savedApplicationForm = merchantApplicationFormRepositoryPort.save(applicationForm);

        return RejectMerchantApplicationResult.builder()
                .applicationId(savedApplicationForm.getId())
                .formCode(savedApplicationForm.getFormCode())
                .status(savedApplicationForm.getStatus().name())
                .rejectedBy(savedApplicationForm.getRejectedBy())
                .rejectionReason(savedApplicationForm.getRejectionReason())
                .build();
    }

    private String resolveSlug(SubmitMerchantApplicationCommand command) {
        if (command.slug() != null && !command.slug().isBlank()) {
            return command.slug().trim();
        }
        return command.displayName()
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
    private void validatePendingApplication(MerchantApplicationForm applicationForm, RequestContext context) {
        if (applicationForm.getStatus() != ApplicationFormStatus.SUBMITTED) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, "Merchant application form has already been reviewed")
            );
        }
    }

    private String buildMerchantAddress(MerchantApplicationForm applicationForm) {
        return String.join(", ",
                applicationForm.getProvince(),
                applicationForm.getCountry()
        );
    }

    private BusinessException notFound(RequestContext context) {
        return new BusinessException(
                context.requestId(),
                context.requestDateTime(),
                context.channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Merchant application form not found")
        );
    }

    private void assignMerchantOwnerRole(String submittedBy, RequestContext context) {
        UserAccountReference userAccount = userAccountLookupPort.findByEmail(submittedBy)
                .orElseThrow(() -> new BusinessException(
                        context.requestId(),
                        context.requestDateTime(),
                        context.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Submitted user account not found")
                ));

        RoleAggregate merchantOwnerRole = roleRepositoryPort.findByCode(MERCHANT_OWNER_ROLE_CODE)
                .orElseThrow(() -> new BusinessException(
                        context.requestId(),
                        context.requestDateTime(),
                        context.channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "MERCHANT_OWNER role not found")
                ));

        if (!Boolean.TRUE.equals(merchantOwnerRole.getEnabled())) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "MERCHANT_OWNER role is inactive")
            );
        }

        if (!userRoleAssignmentRepositoryPort.exists(userAccount.id(), merchantOwnerRole.getId())) {
            userRoleAssignmentRepositoryPort.save(
                    UserRoleAssignment.assign(userAccount.id(), merchantOwnerRole.getId(), OffsetDateTime.now())
            );
        }
    }

    private int resolvePageSize(String pageSize, RequestContext context) {
        if (pageSize == null || pageSize.isBlank()) {
            return DEFAULT_PAGE_SIZE;
        }
        int value = parsePositiveInt(pageSize, "pageSize", context);
        if (value > 100) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE)
            );
        }
        return value;
    }

    private int resolvePageNumber(String pageNumber, RequestContext context) {
        if (pageNumber == null || pageNumber.isBlank()) {
            return DEFAULT_PAGE_NUMBER;
        }
        int value = parsePositiveInt(pageNumber, "pageNumber", context);
        if (value < 1) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER)
            );
        }
        return value;
    }

    private int parsePositiveInt(String value, String field, RequestContext context) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            throw new BusinessException(
                    context.requestId(),
                    context.requestDateTime(),
                    context.channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, field + " must be numeric")
            );
        }
    }
}
