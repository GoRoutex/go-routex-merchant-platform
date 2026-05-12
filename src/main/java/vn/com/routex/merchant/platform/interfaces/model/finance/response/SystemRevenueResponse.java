package vn.com.routex.merchant.platform.interfaces.model.finance.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SystemRevenueResponse extends BaseResponse<SystemRevenueResponse.SystemRevenueData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SystemRevenueData {
        private BigDecimal totalRevenue;
        private BigDecimal totalSystemCommission;
        private BigDecimal totalMerchantShare;
        private Integer totalTickets;
        private Integer merchantCount;
    }
}
