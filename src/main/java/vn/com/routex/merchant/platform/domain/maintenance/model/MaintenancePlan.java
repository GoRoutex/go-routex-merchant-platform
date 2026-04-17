package vn.com.routex.merchant.platform.domain.maintenance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MaintenancePlan extends AbstractAuditingEntity {
    private String id;
    private String merchantId;
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
