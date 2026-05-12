package vn.com.routex.merchant.platform.application.command.holiday;

import lombok.Builder;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;

@Builder
public record DeleteHolidayCommand(
        String id,
        RequestContext context
) {}
