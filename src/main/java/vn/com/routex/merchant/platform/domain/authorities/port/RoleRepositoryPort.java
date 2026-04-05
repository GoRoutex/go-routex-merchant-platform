package vn.com.routex.merchant.platform.domain.authorities.port;


import vn.com.routex.merchant.platform.domain.authorities.model.RoleAggregate;

import java.util.Optional;

public interface RoleRepositoryPort {
    boolean existsByCode(String code);

    Optional<RoleAggregate> findById(String roleId);

    void save(RoleAggregate roleAggregate);
}
