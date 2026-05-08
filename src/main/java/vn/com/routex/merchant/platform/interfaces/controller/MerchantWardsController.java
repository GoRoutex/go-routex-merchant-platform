package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.merchant.platform.application.command.wards.FetchWardsQuery;
import vn.com.routex.merchant.platform.application.command.wards.FetchWardsResult;
import vn.com.routex.merchant.platform.application.command.wards.SearchWardsQuery;
import vn.com.routex.merchant.platform.application.command.wards.SearchWardsResult;
import vn.com.routex.merchant.platform.application.service.WardManagementService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.wards.FetchWardsResponse;
import vn.com.routex.merchant.platform.interfaces.model.wards.SearchWardsResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.WARDS;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
public class MerchantWardsController {

    private final ApiResultFactory apiResultFactory;
    private final WardManagementService wardManagementService;

    @GetMapping(WARDS + FETCH_PATH)
    public ResponseEntity<FetchWardsResponse> fetchWards(
            HttpServletRequest servletRequest,
            @RequestParam(required = false) String provinceId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        FetchWardsResult result = wardManagementService.fetchWards(
                FetchWardsQuery.builder()
                        .context(HttpUtils.toContext(baseRequest))
                        .provinceId(provinceId)
                        .pageSize(String.valueOf(pageSize))
                        .pageNumber(String.valueOf(pageNumber))
                        .build()
        );

        List<FetchWardsResponse.FetchWardsResponseData> dataList = result.items().stream()
                .map(w -> FetchWardsResponse.FetchWardsResponseData.builder()
                        .id(w.id())
                        .name(w.name())
                        .provinceId(w.provinceId())
                        .build())
                .collect(Collectors.toList());

        FetchWardsResponse response = FetchWardsResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchWardsResponse.FetchWardsResponsePage.builder()
                        .items(dataList)
                        .pagination(FetchWardsResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(WARDS + SEARCH_PATH)
    public ResponseEntity<SearchWardsResponse> searchWards(
            HttpServletRequest servletRequest,
            @RequestParam String keyword,
            @RequestParam(required = false) String provinceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        SearchWardsResult result = wardManagementService.searchWards(
                SearchWardsQuery.builder()
                        .context(HttpUtils.toContext(baseRequest))
                        .keyword(keyword)
                        .provinceId(provinceId)
                        .page(page)
                        .size(size)
                        .build()
        );

        List<SearchWardsResponse.SearchWardsResponseData> dataList = result.data().stream()
                .map(w -> SearchWardsResponse.SearchWardsResponseData.builder()
                        .id(w.id())
                        .name(w.name())
                        .provinceId(w.provinceId())
                        .build())
                .collect(Collectors.toList());

        SearchWardsResponse response = SearchWardsResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(dataList)
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
