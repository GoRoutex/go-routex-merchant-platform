package vn.com.routex.merchant.platform.domain.vehicle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.merchant.platform.domain.vehicle.FuelType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VehicleTemplate extends AbstractAuditingEntity {
    private String id;
    private String code;
    private String name;
    private String manufacturer;
    private String model;
    private Long seatCapacity;
    private VehicleTemplateCategory category;
    private VehicleTemplateType type;
    private FuelType fuelType;
    private boolean hasFloor;
    private String merchantId;
    private VehicleTemplateStatus status;
}
