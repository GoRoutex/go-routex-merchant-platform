package vn.com.routex.merchant.platform.application.command.holiday;

import lombok.Builder;

@Builder
public record DeleteHolidayResult(
        String id,
        String status
) {}
