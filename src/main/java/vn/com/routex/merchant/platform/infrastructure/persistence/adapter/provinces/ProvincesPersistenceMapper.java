package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.provinces;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.provinces.model.Province;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.ProvincesEntity;

@Component
public class ProvincesPersistenceMapper {
    public Province toDomain(ProvincesEntity entity) {
        if (entity == null) return null;
        return Province.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .build();

    }

    public ProvincesEntity toEntity(Province province) {
        if (province == null) return null;
        return ProvincesEntity.builder()
                .id(province.getId() != null ? province.getId() : 0)
                .name(province.getName())
                .code(province.getCode())
                .build();
    }
}
