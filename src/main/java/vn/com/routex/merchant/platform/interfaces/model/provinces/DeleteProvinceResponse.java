package vn.com.routex.merchant.platform.interfaces.model.provinces;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DeleteProvinceResponse extends BaseResponse<DeleteProvinceResponse.DeleteProvinceResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class DeleteProvinceResponseData {
        private Integer id;
    }
}

