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
public class CreateVehicleTemplateRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateVehicleTemplateRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateVehicleTemplateRequestData {
        @NotBlank
        private String creator;
        @NotBlank
        private String code;
        @NotBlank
        private String name;
        @NotBlank
        private String manufacturer;
        @NotBlank
        private String model;
        @NotNull
        @Min(1)
        private Long seatCapacity;
        @NotNull
        private VehicleTemplateCategory category;
        @NotNull
        private VehicleTemplateType type;
        @NotNull
        private FuelType fuelType;
        @NotNull
        private Boolean hasFloor;
        private VehicleTemplateStatus status;
    }
}
