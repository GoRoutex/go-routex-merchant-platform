package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.merchant.platform.application.command.finance.FetchMerchantRevenueQuery;
import vn.com.routex.merchant.platform.application.command.finance.FetchSystemRevenueQuery;
import vn.com.routex.merchant.platform.application.service.FinanceService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.MerchantRevenueResponse;
import vn.com.routex.merchant.platform.interfaces.model.finance.response.SystemRevenueResponse;

import java.time.OffsetDateTime;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.*;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE + "/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/revenue/merchant")
    public ResponseEntity<MerchantRevenueResponse> getMerchantRevenue(
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            HttpServletRequest servletRequest) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        String resolvedMerchantId = (merchantId != null) ? merchantId : ApiRequestUtils.getMerchantId(servletRequest);

        FetchMerchantRevenueQuery query = FetchMerchantRevenueQuery.builder()
                .merchantId(resolvedMerchantId)
                .startDate(startDate)
                .endDate(endDate)
                .context(ApiRequestUtils.getRequestContext(baseRequest))
                .build();

        MerchantRevenueResponse response = financeService.getMerchantRevenue(query);
        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping("/revenue/system")
    public ResponseEntity<SystemRevenueResponse> getSystemRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            HttpServletRequest servletRequest) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        FetchSystemRevenueQuery query = FetchSystemRevenueQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .context(ApiRequestUtils.getRequestContext(baseRequest))
                .build();

        SystemRevenueResponse response = financeService.getSystemRevenue(query);
        return HttpUtils.buildResponse(baseRequest, response);
    }
}

