package vn.com.routex.merchant.platform.interfaces.model.merchant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FetchMerchantApplicationFormsRequest extends BaseRequest {

    @Valid
    @NotNull
    private FetchMerchantApplicationFormsRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchMerchantApplicationFormsRequestData {
        private String pageSize;
        private String pageNumber;
    }
}
