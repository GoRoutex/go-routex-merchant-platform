package vn.com.routex.merchant.platform.interfaces.model.driver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.OperationStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FetchDriverDetailResponse extends BaseResponse<FetchDriverDetailResponse.FetchDriverDetailResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchDriverDetailResponseData {
        private String id;
        private String merchantId;
        private String userId;
        private String employeeCode;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private DriverStatus status;
        private OperationStatus operationStatus;
        private Double rating;
        private Integer totalTrips;
        private String licenseClass;
        private String licenseNumber;
        private LocalDate licenseIssueDate;
        private LocalDate licenseExpiryDate;
        private Integer pointsDelta;
        private String pointsReason;
        private Boolean kycVerified;
        private Boolean trainingCompleted;
        private String note;
    }
}
