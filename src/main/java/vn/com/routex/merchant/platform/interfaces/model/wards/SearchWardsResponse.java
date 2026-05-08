package vn.com.routex.merchant.platform.interfaces.model.wards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SearchWardsResponse extends BaseResponse<List<SearchWardsResponse.SearchWardsResponseData>> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchWardsResponseData {
        private String id;
        private String name;
        private String provinceId;
    }
}
