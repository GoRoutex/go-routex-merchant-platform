package vn.com.routex.merchant.platform.interfaces.model.vehicletemplate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.vehicle.FuelType;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateVehicleTemplateRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateVehicleTemplateRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateVehicleTemplateRequestData {
        @NotBlank
        private String creator;
        @NotBlank
        private String templateId;
        private String code;
        private String name;
        private String manufacturer;
        private String model;
        @Min(1)
        private Long seatCapacity;
        private VehicleTemplateCategory category;
        private VehicleTemplateType type;
        private FuelType fuelType;
        private Boolean hasFloor;
        private VehicleTemplateStatus status;
    }
}
