package vn.com.routex.merchant.platform.application.command.driver;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.OperationStatus;

@Builder
public record DeleteDriverResult(
        String id,
        DriverStatus status,
        OperationStatus operationStatus
) {
}
