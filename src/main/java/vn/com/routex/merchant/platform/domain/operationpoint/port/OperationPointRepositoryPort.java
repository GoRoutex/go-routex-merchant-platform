package vn.com.routex.merchant.platform.domain.operationpoint.port;


import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.operationpoint.model.OperationPoint;

import java.util.Optional;

public interface OperationPointRepositoryPort {
    Optional<OperationPoint> findByCode(String code);

    Optional<OperationPoint> findById(String id);

    boolean existsByCode(String code);

    void save(OperationPoint operationPoint);

    PagedResult<OperationPoint> fetch(int pageNumber, int pageSize);
}
