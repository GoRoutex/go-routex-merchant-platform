package vn.com.routex.merchant.platform.application.command.driver;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.OperationStatus;

import java.time.LocalDate;

@Builder
public record CreateDriverResult(
        String id,
        String merchantId,
        String creator,
        String userId,
        String employeeCode,
        String emergencyContactName,
        String emergencyContactPhone,
        DriverStatus status,
        OperationStatus operationStatus,
        Double rating,
        Integer totalTrips,
        String licenseClass,
        String licenseNumber,
        LocalDate licenseIssueDate,
        LocalDate licenseExpiryDate,
        Integer pointsDelta,
        String pointsReason,
        Boolean kycVerified,
        Boolean trainingCompleted,
        String note
) {
}
