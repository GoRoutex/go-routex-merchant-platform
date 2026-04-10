package vn.com.routex.merchant.platform.interfaces.model.merchant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class FetchMerchantApplicationFormDetailRequest extends BaseRequest {

    @Valid
    @NotNull
    private FetchMerchantApplicationFormDetailRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class FetchMerchantApplicationFormDetailRequestData {
        @NotBlank
        private String applicationFormId;
    }
}
