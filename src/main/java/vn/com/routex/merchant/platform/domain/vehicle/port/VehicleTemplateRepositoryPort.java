package vn.com.routex.merchant.platform.domain.vehicle.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VehicleTemplateRepositoryPort {
    Optional<VehicleTemplate> findByCategoryAndType(String category, String type);

    Optional<VehicleTemplate> findByCategoryAndType(String category, String type, String merchantId);

    Optional<VehicleTemplate> findById(String id);

    Optional<VehicleTemplate> findById(String id, String merchantId);

    Map<String, VehicleTemplate> findByIds(List<String> ids);

    boolean existsByCode(String code, String merchantId);

    boolean existsByCategoryAndType(String category, String type, String merchantId);

    void save(VehicleTemplate template);

    PagedResult<VehicleTemplate> fetch(
            String merchantId,
            vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus status,
            vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory category,
            vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType type,
            int pageNumber,
            int pageSize
    );
}
