package vn.com.routex.merchant.platform.interfaces.model.maintenance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateMaintenancePlanRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateMaintenancePlanRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateMaintenancePlanRequestData {
        @NotBlank
        private String creator;
        @NotBlank
        private String vehicleId;
        @NotBlank
        private String code;
        @NotBlank
        private String title;
        private String description;
        @NotNull
        private MaintenancePlanType type;
        private LocalDate plannedDate;
        private LocalDate dueDate;
        private Long currentOdometerKm;
        private Long targetOdometerKm;
        private BigDecimal estimatedCost;
        private String serviceProvider;
        private String note;
    }
}
