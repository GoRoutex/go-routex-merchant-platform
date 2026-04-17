package vn.com.routex.merchant.platform.interfaces.model.maintenance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
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
    private String creator;
    private String vehicleId;
    private String code;
    private String title;
    private String description;
    private MaintenancePlanType type;
    private LocalDate plannedDate;
    private LocalDate dueDate;
    private Long currentOdometerKm;
    private Long targetOdometerKm;
    private BigDecimal estimatedCost;
    private String serviceProvider;
    private String note;
}
