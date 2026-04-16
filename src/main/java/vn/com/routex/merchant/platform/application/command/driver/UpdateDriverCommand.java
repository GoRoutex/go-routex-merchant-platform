package vn.com.routex.merchant.platform.application.command.driver;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

public record UpdateDriverCommand(
        RequestContext context,
        String merchantId,
        String creator,
        String driverId,
        String userId,
        String employeeCode,
        String emergencyContactName,
        String emergencyContactPhone,
        String status,
        String operationStatus,
        String rating,
        String totalTrips,
        String licenseClass,
        String licenseNumber,
        String licenseIssueDate,
        String licenseExpiryDate,
        String pointsDelta,
        String pointsReason,
        Boolean kycVerified,
        Boolean trainingCompleted,
        String note
) {
    @Builder
    public UpdateDriverCommand {
    }
}
