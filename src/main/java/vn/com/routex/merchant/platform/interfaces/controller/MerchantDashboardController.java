package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.merchant.platform.application.command.dashboard.FetchMerchantDashboardQuery;
import vn.com.routex.merchant.platform.application.service.DashboardService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.dashboard.response.MerchantDashboardResponse;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE + "/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('merchant:dashboard') or hasRole('MERCHANT_ADMIN')")
public class MerchantDashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<MerchantDashboardResponse> getDashboard(
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false, defaultValue = "DAY") String filterType,
            HttpServletRequest servletRequest) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String resolvedMerchantId = (merchantId != null) ? merchantId : ApiRequestUtils.getMerchantId(servletRequest);

        FetchMerchantDashboardQuery query = FetchMerchantDashboardQuery.builder()
                .merchantId(resolvedMerchantId)
                .filterType(filterType)
                .context(ApiRequestUtils.getRequestContext(baseRequest))
                .build();

        MerchantDashboardResponse response = dashboardService.getDashboard(query);
        return HttpUtils.buildResponse(baseRequest, response);
    }

}
