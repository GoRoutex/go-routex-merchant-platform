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
public class SubmitMerchantApplicationResponse extends BaseResponse<SubmitMerchantApplicationResponse.SubmitMerchantApplicationResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SubmitMerchantApplicationResponseData {
        private String applicationId;
        private String formCode;
        private String displayName;
        private String legalName;
        private String status;
        private OffsetDateTime submittedAt;
    }
}
