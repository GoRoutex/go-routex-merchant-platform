package vn.com.routex.merchant.platform.interfaces.model.internal.merchant;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InternalFetchMerchantsByIdsRequest {
    private List<String> merchantIds;
}
