package vn.com.routex.merchant.platform.interfaces.model.holiday.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class HolidayResponse extends BaseResponse<HolidayResponse.HolidayData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class HolidayData {
        private String id;
        private LocalDate holidayDate;
        private String name;
        private Boolean isPeakDay;
        private BigDecimal surchargeRate;
        private String description;
    }
}
