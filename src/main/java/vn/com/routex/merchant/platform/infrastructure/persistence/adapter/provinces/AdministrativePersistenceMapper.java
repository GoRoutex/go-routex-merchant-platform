package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.provinces;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.provinces.model.District;
import vn.com.routex.merchant.platform.domain.provinces.model.Ward;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.DistrictsEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.provinces.entity.WardsEntity;

@Component
public class AdministrativePersistenceMapper {

    public District toDomain(DistrictsEntity entity) {
        if (entity == null) return null;
        return District.builder()
                .id(entity.getId())
                .name(entity.getName())
                .provinceId(entity.getProvinceId())
                .build();
    }

    public DistrictsEntity toEntity(District domain) {
        if (domain == null) return null;
        return DistrictsEntity.builder()
                .id(domain.getId() != null ? domain.getId() : 0)
                .name(domain.getName())
                .provinceId(domain.getProvinceId())
                .build();
    }

    public Ward toDomain(WardsEntity entity) {
        if (entity == null) return null;
        return Ward.builder()
                .id(entity.getId())
                .name(entity.getName())
                .districtId(entity.getDistrictId())
                .build();
    }

    public WardsEntity toEntity(Ward domain) {
        if (domain == null) return null;
        return WardsEntity.builder()
                .id(domain.getId() != null ? domain.getId() : 0)
                .name(domain.getName())
                .districtId(domain.getDistrictId())
                .build();
    }
}
