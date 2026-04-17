package vn.com.routex.merchant.platform.infrastructure.persistence.exception;

import lombok.EqualsAndHashCode;
import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.interfaces.model.result.ApiResult;

@EqualsAndHashCode(callSuper = true)
public class BusinessException extends BaseException {

    public BusinessException(String requestId, String requestDateTime, String channel, ApiResult result) {
        super(requestId, requestDateTime, channel, result);
    }

    public BusinessException(RequestContext context, ApiResult result) {
        super(context.requestId(), context.requestDateTime(), context.channel(), result);
    }

    public BusinessException(ApiResult result) { super(result); }
}
