package vn.com.routex.merchant.platform.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.operationpoint.CreateOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.CreateOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.DeleteOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.DeleteOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.FetchOperationPointQuery;
import vn.com.routex.merchant.platform.application.command.operationpoint.FetchOperationPointResult;
import vn.com.routex.merchant.platform.application.command.operationpoint.UpdateOperationPointCommand;
import vn.com.routex.merchant.platform.application.command.operationpoint.UpdateOperationPointResult;
import vn.com.routex.merchant.platform.application.service.OperationPointManagementService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.CreateOperationPointRequest;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.CreateOperationPointResponse;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.DeleteOperationPointRequest;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.DeleteOperationPointResponse;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.FetchOperationPointResponse;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.UpdateOperationPointRequest;
import vn.com.routex.merchant.platform.interfaces.model.operationpoint.UpdateOperationPointResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.OPERATION_POINT;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
public class OperationPointManagementController {

    private final OperationPointManagementService operationPointManagementService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @PostMapping(OPERATION_POINT + CREATE_PATH)
    @PreAuthorize("hasAuthority('points:management') or hasRole('ADMIN')")
    public ResponseEntity<CreateOperationPointResponse> createOperationPoint(@Valid @RequestBody CreateOperationPointRequest request,
                                                                             HttpServletRequest servletRequest) {

        sLog.info("[OPERATION-POINT] Create Operation Point Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        CreateOperationPointResult result = operationPointManagementService.createOperationPoint(CreateOperationPointCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .type(request.getData().getType())
                .address(request.getData().getAddress())
                .city(request.getData().getCity())
                .latitude(request.getData().getLatitude())
                .longitude(request.getData().getLongitude())
                .status(request.getData().getStatus())
                .build());

        CreateOperationPointResponse response = CreateOperationPointResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(CreateOperationPointResponse.CreateOperationPointResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .name(result.name())
                        .type(result.type())
                        .address(result.address())
                        .city(result.city())
                        .latitude(result.latitude())
                        .longitude(result.longitude())
                        .status(result.status())
                        .build())
                .build();


        sLog.info("[OPERATION-POINT] Create Operation Point Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(OPERATION_POINT + UPDATE_PATH)
    @PreAuthorize("hasAuthority('points:management') or hasRole('ADMIN')")
    public ResponseEntity<UpdateOperationPointResponse> updateOperationPoint(@Valid @RequestBody UpdateOperationPointRequest request,
                                                                             HttpServletRequest servletRequest) {

        sLog.info("[OPERATION-POINT] Update Operation Point Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);

        UpdateOperationPointResult result = operationPointManagementService.updateOperationPoint(UpdateOperationPointCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .id(request.getData().getId())
                .code(request.getData().getCode())
                .name(request.getData().getName())
                .type(request.getData().getType())
                .address(request.getData().getAddress())
                .city(request.getData().getCity())
                .latitude(request.getData().getLatitude())
                .longitude(request.getData().getLongitude())
                .status(request.getData().getStatus())
                .build());

        UpdateOperationPointResponse response = UpdateOperationPointResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(UpdateOperationPointResponse.UpdateOperationPointResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .name(result.name())
                        .type(result.type())
                        .address(result.address())
                        .city(result.city())
                        .latitude(result.latitude())
                        .longitude(result.longitude())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[OPERATION-POINT] Update Operation Point Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(OPERATION_POINT + DELETE_PATH)
    @PreAuthorize("hasAuthority('points:management') or hasRole('ADMIN')")
    public ResponseEntity<DeleteOperationPointResponse> deleteOperationPoint(@Valid @RequestBody DeleteOperationPointRequest request,
                                                                             HttpServletRequest servletRequest) {

        sLog.info("[OPERATION-POINT] Delete Operation Point Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);

        DeleteOperationPointResult result = operationPointManagementService.deleteOperationPoint(DeleteOperationPointCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .id(request.getData().getId())
                .build());

        DeleteOperationPointResponse response = DeleteOperationPointResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(DeleteOperationPointResponse.DeleteOperationPointResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[OPERATION-POINT] Delete Operation Point Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(OPERATION_POINT + FETCH_PATH)
    @PreAuthorize("hasAuthority('points:management') or hasRole('ADMIN')")
    public ResponseEntity<FetchOperationPointResponse> fetchOperationPoint(@RequestParam int pageNumber,
                                                                           @RequestParam int pageSize,
                                                                           HttpServletRequest servletRequest) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchOperationPointResult result = operationPointManagementService.fetchOperationPoint(FetchOperationPointQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .merchantId(merchantId)
                .build());

        List<FetchOperationPointResponse.FetchOperationPointResponseData> items = result.items().stream()
                .map(p -> FetchOperationPointResponse.FetchOperationPointResponseData.builder()
                        .id(p.id())
                        .code(p.code())
                        .name(p.name())
                        .type(p.type())
                        .address(p.address())
                        .city(p.city())
                        .latitude(p.latitude())
                        .longitude(p.longitude())
                        .status(p.status())
                        .build())
                .collect(Collectors.toList());

        FetchOperationPointResponse response = FetchOperationPointResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchOperationPointResponse.FetchOperationPointResponsePage.builder()
                        .items(items)
                        .pagination(FetchOperationPointResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
