package vn.com.routex.merchant.platform.interfaces.factory;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.interfaces.model.result.ApiResult;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.SUCCESS_CODE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.SUCCESS_MESSAGE;

@Component
public class ApiResultFactory {
    public ApiResult buildSuccess() {
        return ApiResult.builder()
                .responseCode(SUCCESS_CODE)
                .description(SUCCESS_MESSAGE)
                .build();
    }
}
