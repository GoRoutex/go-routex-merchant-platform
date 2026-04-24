package vn.com.routex.merchant.platform.domain.driver.port;


import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.driver.DriverStatus;
import vn.com.routex.merchant.platform.domain.driver.model.DriverProfile;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port (no Spring Data/JPA dependency).
 * Infrastructure layer provides an adapter implementation.
 */
public interface DriverProfileRepositoryPort {
    Optional<DriverProfile> findById(String id);
    Optional<DriverProfile> findById(String id, String merchantId);
    Optional<DriverProfile> findByUserId(String userId);
    Optional<DriverProfile> findByUserId(String userId, String merchantId);
    Optional<DriverProfile> findByEmployeeCode(String employeeCode, String merchantId);
    boolean existsByUserId(String userId, String merchantId);
    boolean existsByEmployeeCode(String employeeCode, String merchantId);
    List<DriverProfile> findByMerchantId(String merchantId);
    PagedResult<DriverProfile> fetch(String merchantId, int pageNumber, int pageSize);
    PagedResult<DriverProfile> fetch(String merchantId, DriverStatus status, int pageNumber, int pageSize);

    DriverProfile save(DriverProfile profile);
}
