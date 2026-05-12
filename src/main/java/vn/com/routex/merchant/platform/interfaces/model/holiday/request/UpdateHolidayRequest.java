package vn.com.routex.merchant.platform.interfaces.model.holiday.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateHolidayRequest extends BaseRequest {
    private UpdateHolidayData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateHolidayData {
        private String id; // Optional if passed in Path, but good for body consistency
        private LocalDate holidayDate;
        private String name;
        private Boolean isPeakDay;
        private BigDecimal surchargeRate;
        private String description;
    }
}
