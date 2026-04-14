package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantUser;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantUserEntity;

@Component
public class MerchantUserPersistenceMapper {

    public MerchantUserEntity toEntity(MerchantUser merchantUser) {
        return MerchantUserEntity.builder()
                .id(merchantUser.getId())
                .merchantId(merchantUser.getMerchantId())
                .userId(merchantUser.getUserId())
                .roleCode(merchantUser.getRoleCode())
                .status(merchantUser.getStatus())
                .createdAt(merchantUser.getCreatedAt())
                .createdBy(merchantUser.getCreatedBy())
                .updatedAt(merchantUser.getUpdatedAt())
                .updatedBy(merchantUser.getUpdatedBy())
                .build();
    }

    public MerchantUser toDomain(MerchantUserEntity entity) {
        return MerchantUser.builder()
                .id(entity.getId())
                .merchantId(entity.getMerchantId())
                .userId(entity.getUserId())
                .roleCode(entity.getRoleCode())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
