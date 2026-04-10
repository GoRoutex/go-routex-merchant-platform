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
public class UpdateProvinceResponse extends BaseResponse<UpdateProvinceResponse.UpdateProvinceResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateProvinceResponseData {
        private Integer id;
        private String name;
        private String code;
    }
}

