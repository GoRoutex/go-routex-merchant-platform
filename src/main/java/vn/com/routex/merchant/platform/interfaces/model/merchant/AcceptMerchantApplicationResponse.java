package vn.com.routex.merchant.platform.interfaces.model.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AcceptMerchantApplicationResponse extends BaseResponse<AcceptMerchantApplicationResponse.AcceptMerchantApplicationResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AcceptMerchantApplicationResponseData {
        private String applicationId;
        private String formCode;
        private String merchantId;
        private String merchantCode;
        private String merchantName;
        private String status;
        private String approvedBy;
        private OffsetDateTime approvedAt;
    }
}
