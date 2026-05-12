package vn.com.routex.merchant.platform.application.command.holiday;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CreateHolidayCommand(
        LocalDate holidayDate,
        String name,
        Boolean isPeakDay,
        BigDecimal surchargeRate,
        String description,
        RequestContext context
) {}
