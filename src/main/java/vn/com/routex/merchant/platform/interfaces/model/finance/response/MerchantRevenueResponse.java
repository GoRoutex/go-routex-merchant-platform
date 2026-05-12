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
public class MerchantRevenueResponse extends BaseResponse<MerchantRevenueResponse.MerchantRevenueData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class MerchantRevenueData {
        private String merchantId;
        private BigDecimal totalRevenue;
        private BigDecimal merchantShare;
        private BigDecimal systemCommission;
        private Integer ticketCount;
    }
}
