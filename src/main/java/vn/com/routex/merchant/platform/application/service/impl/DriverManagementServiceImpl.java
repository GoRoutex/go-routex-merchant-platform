package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.driver.CreateDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.CreateDriverResult;
import vn.com.routex.merchant.platform.application.command.driver.DeleteDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.DeleteDriverResult;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriverDetailQuery;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriverDetailResult;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriversQuery;
import vn.com.routex.merchant.platform.application.command.driver.FetchDriversResult;
import vn.com.routex.merchant.platform.application.command.driver.UpdateDriverCommand;
import vn.com.routex.merchant.platform.application.command.driver.UpdateDriverResult;
import vn.com.routex.merchant.platform.application.service.DriverManagementService;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.OperationStatus;
import vn.com.routex.merchant.platform.domain.driver.model.DriverProfile;
import vn.com.routex.merchant.platform.domain.driver.port.DriverProfileRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DRIVER_NOT_FOUND_BY_EMPLOYEE_CODE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DRIVER_NOT_FOUND_BY_ID;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DRIVER_NOT_FOUND_BY_USER_ID;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_DRIVER_BY_EMPLOYEE_CODE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_DRIVER_BY_USER_ID;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DriverManagementServiceImpl implements DriverManagementService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private final DriverProfileRepositoryPort driverProfileRepositoryPort;

    @Override
    @Transactional
    public CreateDriverResult createDriver(CreateDriverCommand command) {
        validateDuplicateUserId(command.userId(), command.merchantId(), command);
        validateDuplicateEmployeeCode(command.employeeCode(), command.merchantId(), command);

        OffsetDateTime now = OffsetDateTime.now();
        DriverProfile driverProfile = DriverProfile.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(command.merchantId())
                .userId(normalize(command.userId()))
                .employeeCode(normalize(command.employeeCode()))
                .emergencyContactName(normalize(command.emergencyContactName()))
                .emergencyContactPhone(normalize(command.emergencyContactPhone()))
                .status(parseDriverStatus(command.status(), DriverStatus.ACTIVE))
                .operationStatus(parseOperationStatus(command.operationStatus(), OperationStatus.OFFLINE))
                .rating(parseDouble(command.rating(), 0D))
                .totalTrips(parseInteger(command.totalTrips(), 0))
                .licenseClass(normalize(command.licenseClass()))
                .licenseNumber(normalize(command.licenseNumber()))
                .licenseIssueDate(parseLocalDate(command.licenseIssueDate()))
                .licenseExpiryDate(parseLocalDate(command.licenseExpiryDate()))
                .pointsDelta(parseInteger(command.pointsDelta(), 0))
                .pointsReason(normalize(command.pointsReason()))
                .kycVerified(command.kycVerified() != null ? command.kycVerified() : Boolean.FALSE)
                .trainingCompleted(command.trainingCompleted() != null ? command.trainingCompleted() : Boolean.FALSE)
                .note(normalize(command.note()))
                .createdAt(now)
                .createdBy(command.creator())
                .updatedAt(now)
                .updatedBy(command.creator())
                .build();

        DriverProfile saved = driverProfileRepositoryPort.save(driverProfile);
        return toCreateResult(saved);
    }

    @Override
    @Transactional
    public UpdateDriverResult updateDriver(UpdateDriverCommand command) {
        DriverProfile existing = findDriverForUpdate(command);
        validateUpdateUniqueness(command, existing);
        DriverProfile updated = mergeUpdatedDriver(existing, command);

        DriverProfile saved = driverProfileRepositoryPort.save(updated);
        return toUpdateResult(saved);
    }

    @Override
    @Transactional
    public DeleteDriverResult deleteDriver(DeleteDriverCommand command) {
        DriverProfile existing = driverProfileRepositoryPort.findById(command.driverId(), command.merchantId())
                .orElseThrow(() -> notFound(command.driverId(), DRIVER_NOT_FOUND_BY_ID, command));

        DriverProfile deleted = existing.toBuilder()
                .status(DriverStatus.DELETED)
                .operationStatus(OperationStatus.OFFLINE)
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.creator())
                .build();

        DriverProfile saved = driverProfileRepositoryPort.save(deleted);
        return DeleteDriverResult.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .operationStatus(saved.getOperationStatus())
                .build();
    }

    @Override
    public FetchDriversResult fetchDrivers(FetchDriversQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        PagedResult<DriverProfile> page = driverProfileRepositoryPort.fetch(query.merchantId(), pageNumber - 1, pageSize);
        List<FetchDriversResult.FetchDriverItemResult> items = page.getItems().stream()
                .map(this::toFetchItem)
                .toList();

        return FetchDriversResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public FetchDriverDetailResult fetchDriverDetail(FetchDriverDetailQuery query) {
        DriverProfile driverProfile = resolveDriver(query)
                .orElseThrow(() -> notFound(buildLookupValue(query), buildLookupMessage(query), query));

        return FetchDriverDetailResult.builder()
                .id(driverProfile.getId())
                .merchantId(driverProfile.getMerchantId())
                .userId(driverProfile.getUserId())
                .employeeCode(driverProfile.getEmployeeCode())
                .emergencyContactName(driverProfile.getEmergencyContactName())
                .emergencyContactPhone(driverProfile.getEmergencyContactPhone())
                .status(driverProfile.getStatus())
                .operationStatus(driverProfile.getOperationStatus())
                .rating(driverProfile.getRating())
                .totalTrips(driverProfile.getTotalTrips())
                .licenseClass(driverProfile.getLicenseClass())
                .licenseNumber(driverProfile.getLicenseNumber())
                .licenseIssueDate(driverProfile.getLicenseIssueDate())
                .licenseExpiryDate(driverProfile.getLicenseExpiryDate())
                .pointsDelta(driverProfile.getPointsDelta())
                .pointsReason(driverProfile.getPointsReason())
                .kycVerified(driverProfile.getKycVerified())
                .trainingCompleted(driverProfile.getTrainingCompleted())
                .note(driverProfile.getNote())
                .build();
    }

    private void validateDuplicateUserId(String userId, String merchantId, CreateDriverCommand command) {
        if (driverProfileRepositoryPort.existsByUserId(userId.trim(), merchantId)) {
            throw duplicate(command, String.format(DUPLICATE_DRIVER_BY_USER_ID, userId.trim()));
        }
    }

    private void validateDuplicateEmployeeCode(String employeeCode, String merchantId, CreateDriverCommand command) {
        if (hasText(employeeCode) && driverProfileRepositoryPort.existsByEmployeeCode(employeeCode.trim(), merchantId)) {
            throw duplicate(command, String.format(DUPLICATE_DRIVER_BY_EMPLOYEE_CODE, employeeCode.trim()));
        }
    }

    private DriverProfile findDriverForUpdate(UpdateDriverCommand command) {
        return driverProfileRepositoryPort.findById(command.driverId(), command.merchantId())
                .orElseThrow(() -> notFound(command.driverId(), DRIVER_NOT_FOUND_BY_ID, command));
    }

    private void validateUpdateUniqueness(UpdateDriverCommand command, DriverProfile existing) {
        validateUpdatedUserId(command, existing);
        validateUpdatedEmployeeCode(command, existing);
    }

    private void validateUpdatedUserId(UpdateDriverCommand command, DriverProfile existing) {
        if (!hasNewUserId(command, existing)) {
            return;
        }

        String userId = command.userId().trim();
        if (driverProfileRepositoryPort.existsByUserId(userId, command.merchantId())) {
            throw duplicate(command, String.format(DUPLICATE_DRIVER_BY_USER_ID, userId));
        }
    }

    private void validateUpdatedEmployeeCode(UpdateDriverCommand command, DriverProfile existing) {
        if (!hasNewEmployeeCode(command, existing)) {
            return;
        }

        String employeeCode = command.employeeCode().trim();
        if (driverProfileRepositoryPort.existsByEmployeeCode(employeeCode, command.merchantId())) {
            throw duplicate(command, String.format(DUPLICATE_DRIVER_BY_EMPLOYEE_CODE, employeeCode));
        }
    }

    private DriverProfile mergeUpdatedDriver(DriverProfile existing, UpdateDriverCommand command) {
        return existing.toBuilder()
                .merchantId(existing.getMerchantId())
                .userId(mergeText(command.userId(), existing.getUserId()))
                .employeeCode(mergeText(command.employeeCode(), existing.getEmployeeCode()))
                .emergencyContactName(mergeText(command.emergencyContactName(), existing.getEmergencyContactName()))
                .emergencyContactPhone(mergeText(command.emergencyContactPhone(), existing.getEmergencyContactPhone()))
                .status(parseDriverStatus(command.status(), existing.getStatus()))
                .operationStatus(parseOperationStatus(command.operationStatus(), existing.getOperationStatus()))
                .rating(mergeDouble(command.rating(), existing.getRating()))
                .totalTrips(mergeInteger(command.totalTrips(), existing.getTotalTrips()))
                .licenseClass(mergeText(command.licenseClass(), existing.getLicenseClass()))
                .licenseNumber(mergeText(command.licenseNumber(), existing.getLicenseNumber()))
                .licenseIssueDate(mergeLocalDate(command.licenseIssueDate(), existing.getLicenseIssueDate()))
                .licenseExpiryDate(mergeLocalDate(command.licenseExpiryDate(), existing.getLicenseExpiryDate()))
                .pointsDelta(mergeInteger(command.pointsDelta(), existing.getPointsDelta()))
                .pointsReason(mergeText(command.pointsReason(), existing.getPointsReason()))
                .kycVerified(mergeBoolean(command.kycVerified(), existing.getKycVerified()))
                .trainingCompleted(mergeBoolean(command.trainingCompleted(), existing.getTrainingCompleted()))
                .note(mergeText(command.note(), existing.getNote()))
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.creator())
                .build();
    }

    private FetchDriversResult.FetchDriverItemResult toFetchItem(DriverProfile driverProfile) {
        return FetchDriversResult.FetchDriverItemResult.builder()
                .id(driverProfile.getId())
                .merchantId(driverProfile.getMerchantId())
                .userId(driverProfile.getUserId())
                .employeeCode(driverProfile.getEmployeeCode())
                .emergencyContactName(driverProfile.getEmergencyContactName())
                .emergencyContactPhone(driverProfile.getEmergencyContactPhone())
                .status(driverProfile.getStatus())
                .operationStatus(driverProfile.getOperationStatus())
                .rating(driverProfile.getRating())
                .totalTrips(driverProfile.getTotalTrips())
                .licenseClass(driverProfile.getLicenseClass())
                .licenseNumber(driverProfile.getLicenseNumber())
                .licenseIssueDate(driverProfile.getLicenseIssueDate())
                .licenseExpiryDate(driverProfile.getLicenseExpiryDate())
                .pointsDelta(driverProfile.getPointsDelta())
                .pointsReason(driverProfile.getPointsReason())
                .kycVerified(driverProfile.getKycVerified())
                .trainingCompleted(driverProfile.getTrainingCompleted())
                .note(driverProfile.getNote())
                .build();
    }

    private CreateDriverResult toCreateResult(DriverProfile saved) {
        return CreateDriverResult.builder()
                .id(saved.getId())
                .merchantId(saved.getMerchantId())
                .creator(saved.getCreatedBy())
                .userId(saved.getUserId())
                .employeeCode(saved.getEmployeeCode())
                .emergencyContactName(saved.getEmergencyContactName())
                .emergencyContactPhone(saved.getEmergencyContactPhone())
                .status(saved.getStatus())
                .operationStatus(saved.getOperationStatus())
                .rating(saved.getRating())
                .totalTrips(saved.getTotalTrips())
                .licenseClass(saved.getLicenseClass())
                .licenseNumber(saved.getLicenseNumber())
                .licenseIssueDate(saved.getLicenseIssueDate())
                .licenseExpiryDate(saved.getLicenseExpiryDate())
                .pointsDelta(saved.getPointsDelta())
                .pointsReason(saved.getPointsReason())
                .kycVerified(saved.getKycVerified())
                .trainingCompleted(saved.getTrainingCompleted())
                .note(saved.getNote())
                .build();
    }

    private UpdateDriverResult toUpdateResult(DriverProfile saved) {
        return UpdateDriverResult.builder()
                .id(saved.getId())
                .merchantId(saved.getMerchantId())
                .creator(saved.getUpdatedBy())
                .userId(saved.getUserId())
                .employeeCode(saved.getEmployeeCode())
                .emergencyContactName(saved.getEmergencyContactName())
                .emergencyContactPhone(saved.getEmergencyContactPhone())
                .status(saved.getStatus())
                .operationStatus(saved.getOperationStatus())
                .rating(saved.getRating())
                .totalTrips(saved.getTotalTrips())
                .licenseClass(saved.getLicenseClass())
                .licenseNumber(saved.getLicenseNumber())
                .licenseIssueDate(saved.getLicenseIssueDate())
                .licenseExpiryDate(saved.getLicenseExpiryDate())
                .pointsDelta(saved.getPointsDelta())
                .pointsReason(saved.getPointsReason())
                .kycVerified(saved.getKycVerified())
                .trainingCompleted(saved.getTrainingCompleted())
                .note(saved.getNote())
                .build();
    }

    private Optional<DriverProfile> resolveDriver(FetchDriverDetailQuery query) {
        if (hasText(query.driverId())) {
            return driverProfileRepositoryPort.findById(query.driverId().trim(), query.merchantId());
        }
        if (hasText(query.userId())) {
            return driverProfileRepositoryPort.findByUserId(query.userId().trim(), query.merchantId());
        }
        if (hasText(query.employeeCode())) {
            return driverProfileRepositoryPort.findByEmployeeCode(query.employeeCode().trim(), query.merchantId());
        }
        throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "driverId or userId or employeeCode is required"));
    }

    private String buildLookupMessage(FetchDriverDetailQuery query) {
        if (hasText(query.driverId())) {
            return DRIVER_NOT_FOUND_BY_ID;
        }
        if (hasText(query.userId())) {
            return DRIVER_NOT_FOUND_BY_USER_ID;
        }
        return DRIVER_NOT_FOUND_BY_EMPLOYEE_CODE;
    }

    private String buildLookupValue(FetchDriverDetailQuery query) {
        if (hasText(query.driverId())) {
            return query.driverId().trim();
        }
        if (hasText(query.userId())) {
            return query.userId().trim();
        }
        return query.employeeCode().trim();
    }

    private BusinessException duplicate(CreateDriverCommand command, String message) {
        return new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, message));
    }

    private BusinessException duplicate(UpdateDriverCommand command, String message) {
        return new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, message));
    }

    private BusinessException notFound(String lookupValue, String template, CreateDriverCommand command) {
        return new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(template, lookupValue)));
    }

    private BusinessException notFound(String lookupValue, String template, UpdateDriverCommand command) {
        return new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(template, lookupValue)));
    }

    private BusinessException notFound(String lookupValue, String template, DeleteDriverCommand command) {
        return new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(template, lookupValue)));
    }

    private BusinessException notFound(String lookupValue, String template, FetchDriverDetailQuery query) {
        return new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(template, lookupValue)));
    }

    private boolean hasNewUserId(UpdateDriverCommand command, DriverProfile existing) {
        return hasText(command.userId()) && !command.userId().trim().equals(existing.getUserId());
    }

    private boolean hasNewEmployeeCode(UpdateDriverCommand command, DriverProfile existing) {
        return hasText(command.employeeCode()) && !command.employeeCode().trim().equals(existing.getEmployeeCode());
    }

    private DriverStatus parseDriverStatus(String value, DriverStatus defaultValue) {
        return value == null || value.isBlank() ? defaultValue : DriverStatus.valueOf(value.trim());
    }

    private OperationStatus parseOperationStatus(String value, OperationStatus defaultValue) {
        return value == null || value.isBlank() ? defaultValue : OperationStatus.valueOf(value.trim());
    }

    private Integer parseInteger(String value, Integer defaultValue) {
        return value == null || value.isBlank() ? defaultValue : Integer.valueOf(value.trim());
    }

    private Double parseDouble(String value, Double defaultValue) {
        return value == null || value.isBlank() ? defaultValue : Double.valueOf(value.trim());
    }

    private LocalDate parseLocalDate(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value.trim());
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String mergeText(String candidate, String currentValue) {
        return ApiRequestUtils.firstNonBlank(candidate, currentValue);
    }

    private Integer mergeInteger(String candidate, Integer currentValue) {
        return candidate == null || candidate.isBlank() ? currentValue : Integer.valueOf(candidate.trim());
    }

    private Double mergeDouble(String candidate, Double currentValue) {
        return candidate == null || candidate.isBlank() ? currentValue : Double.valueOf(candidate.trim());
    }

    private LocalDate mergeLocalDate(String candidate, LocalDate currentValue) {
        return candidate == null || candidate.isBlank() ? currentValue : LocalDate.parse(candidate.trim());
    }

    private Boolean mergeBoolean(Boolean candidate, Boolean currentValue) {
        return candidate == null ? currentValue : candidate;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
