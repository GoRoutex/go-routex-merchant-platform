package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.authorities.model.RoleAggregate;
import vn.com.routex.merchant.platform.domain.authorities.port.RoleRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.authorities.entity.RolesEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.authorities.repository.RolesEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RolesEntityRepository rolesEntityRepository;

    @Override
    public boolean existsByCode(String code) {
        return rolesEntityRepository.existsByCode(code);
    }

    @Override
    public Optional<RoleAggregate> findById(String roleId) {
        return rolesEntityRepository.findById(roleId).map(this::toDomain);
    }

    @Override
    public Optional<RoleAggregate> findByCode(String code) {
        return rolesEntityRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public void save(RoleAggregate roleAggregate) {
        rolesEntityRepository.save(toEntity(roleAggregate));
    }

    private RoleAggregate toDomain(RolesEntity entity) {
        return RoleAggregate.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    private RolesEntity toEntity(RoleAggregate roleAggregate) {
        return RolesEntity.builder()
                .id(roleAggregate.getId())
                .code(roleAggregate.getCode())
                .name(roleAggregate.getName())
                .description(roleAggregate.getDescription())
                .enabled(roleAggregate.getEnabled())
                .createdAt(roleAggregate.getCreatedAt())
                .createdBy(roleAggregate.getCreatedBy())
                .build();
    }
}
