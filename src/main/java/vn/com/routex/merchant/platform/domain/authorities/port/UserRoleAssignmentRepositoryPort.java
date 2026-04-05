package vn.com.routex.merchant.platform.domain.authorities.port;

import vn.com.routex.merchant.platform.domain.authorities.model.UserRoleAssignment;

public interface UserRoleAssignmentRepositoryPort {
    boolean exists(String userId, String roleId);

    void save(UserRoleAssignment assignment);

    void deleteByUserId(String userId);
}
