package vn.com.routex.merchant.platform.domain.department.port;


import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.department.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepositoryPort {


    Optional<Department> findByName(String name, String merchantId);

    Optional<Department> findById(String id);

    Optional<Department> findById(String id, String merchantId);

    boolean existsByName(String name);

    boolean existsByName(String name, String merchantId);

    List<Department> findByMerchantId(String merchantId);

    void save(Department department);

    PagedResult<Department> fetch(int pageNumber, int pageSize);

    PagedResult<Department> fetch(String merchantId, int pageNumber, int pageSize);
}
