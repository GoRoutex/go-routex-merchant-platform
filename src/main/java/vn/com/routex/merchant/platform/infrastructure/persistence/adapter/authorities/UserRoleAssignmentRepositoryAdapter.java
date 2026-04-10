package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.authorities.model.UserRoleAssignment;
import vn.com.routex.merchant.platform.domain.authorities.port.UserRoleAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.authorities.entity.UserRoleIdEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.authorities.entity.UserRolesEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.authorities.repository.UserRolesEntityRepository;

@Component
@RequiredArgsConstructor
public class UserRoleAssignmentRepositoryAdapter implements UserRoleAssignmentRepositoryPort {

    private final UserRolesEntityRepository userRolesEntityRepository;

    @Override
    public boolean exists(String userId, String roleId) {
        return userRolesEntityRepository.existsById(UserRoleIdEntity.builder()
                .userId(userId)
                .roleId(roleId)
                .build());
    }

    @Override
    public void save(UserRoleAssignment assignment) {
        userRolesEntityRepository.save(UserRolesEntity.builder()
                .id(UserRoleIdEntity.builder()
                        .userId(assignment.getUserId())
                        .roleId(assignment.getRoleId())
                        .build())
                .assignedAt(assignment.getAssignedAt())
                .build());
    }

    @Override
    public void deleteByUserId(String userId) {
        userRolesEntityRepository.deleteByIdUserId(userId);
    }
}
