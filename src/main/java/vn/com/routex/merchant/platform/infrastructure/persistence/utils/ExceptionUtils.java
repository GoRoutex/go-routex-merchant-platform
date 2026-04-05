package vn.com.routex.merchant.platform.infrastructure.persistence.utils;


import lombok.experimental.UtilityClass;
import vn.com.routex.merchant.platform.interfaces.model.result.ApiResult;

@UtilityClass
public class ExceptionUtils {

    public ApiResult buildResultResponse(String responseCode, String description) {
        return ApiResult
                .builder()
                .responseCode(responseCode)
                .description(description)
                .build();
    }
}
