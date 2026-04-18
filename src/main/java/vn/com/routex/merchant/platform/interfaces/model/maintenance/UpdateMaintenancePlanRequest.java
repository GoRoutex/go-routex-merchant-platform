package vn.com.routex.merchant.platform.interfaces.model.maintenance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateMaintenancePlanRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateMaintenancePlanRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateMaintenancePlanRequestData {
        @NotBlank
        private String creator;
        @NotBlank
        private String maintenancePlanId;
        private String vehicleId;
        private String code;
        private String title;
        private String description;
        private MaintenancePlanType type;
        private MaintenancePlanStatus status;
        private LocalDate plannedDate;
        private LocalDate dueDate;
        private LocalDate completedDate;
        private Long currentOdometerKm;
        private Long targetOdometerKm;
        private BigDecimal estimatedCost;
        private BigDecimal actualCost;
        private String serviceProvider;
        private String note;
    }
}
