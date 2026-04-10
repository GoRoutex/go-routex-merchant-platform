package vn.com.routex.merchant.platform.interfaces.model.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RejectMerchantApplicationResponse extends BaseResponse<RejectMerchantApplicationResponse.RejectMerchantApplicationResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class RejectMerchantApplicationResponseData {
        private String applicationId;
        private String formCode;
        private String status;
        private String rejectedBy;
        private String rejectionReason;
    }
}
