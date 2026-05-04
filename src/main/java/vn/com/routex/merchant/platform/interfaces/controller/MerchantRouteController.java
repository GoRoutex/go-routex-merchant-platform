package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.CreateRouteResult;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.DeleteRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchDetailRouteQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchDetailRouteResult;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesQuery;
import vn.com.routex.merchant.platform.application.command.route.FetchRoutesResult;
import vn.com.routex.merchant.platform.application.command.route.RoutePointCommand;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.UpdateRouteResult;
import vn.com.routex.merchant.platform.application.service.RouteManagementService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.mapper.RouteResponseMapper;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.route.CreateRouteRequest;
import vn.com.routex.merchant.platform.interfaces.model.route.CreateRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.route.DeleteRouteRequest;
import vn.com.routex.merchant.platform.interfaces.model.route.DeleteRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.route.FetchDetailRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.route.FetchRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.route.SearchRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.route.UpdateRouteRequest;
import vn.com.routex.merchant.platform.interfaces.model.route.UpdateRouteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.ROUTES_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;


@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE + ROUTES_PATH)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('route:management') or hasRole('ADMIN')")
public class MerchantRouteController {


    private final RouteManagementService routeManagementService;
    private final ApiResultFactory apiResultFactory;
    private final RouteResponseMapper routeResponseMapper;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }


    @GetMapping(FETCH_PATH + DETAIL_PATH)
    public ResponseEntity<FetchDetailRouteResponse> fetchDetailRoute(
            HttpServletRequest servletRequest,
            @RequestParam String routeId,
            @RequestParam String merchantId
    ) {

        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);

        FetchDetailRouteResult result =
                routeManagementService.fetchDetailRoute(
                        FetchDetailRouteQuery.builder()
                                .context(HttpUtils.toContext(baseRequest))
                                .routeId(routeId)
                                .merchantId(merchantId)
                                .build()
                );


        FetchDetailRouteResponse response = FetchDetailRouteResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .data(FetchDetailRouteResponse.FetchDetailRouteResponseData
                        .builder()
                        .id(result.id())
                        .creator(result.creator())
                        .originCode(result.originCode())
                        .originName(result.originName())
                        .destinationCode(result.destinationCode())
                        .destinationName(result.destinationName())
                        .status(result.status())
                        .availableSeats(result.availableSeats())
                        .vehicleId(result.vehicleId())
                        .vehiclePlate(result.vehiclePlate())
                        .hasFloor(result.hasFloor())
                        .assignedAt(result.assignedAt())
                        .build())
                .build();

        if(result.routePoints() != null) {
            List<SearchRouteResponse.SearchRoutePoints> routePoints = result.routePoints().stream()
                    .map(point -> SearchRouteResponse.SearchRoutePoints.builder()
                            .id(point.id())
                            .operationOrder(point.operationOrder())
                            .routeId(point.routeId())
                            .note(point.note())
                            .operationPointId(point.operationPointId())
                            .stopName(point.stopName())
                            .stopAddress(point.stopAddress())
                            .stopCity(point.stopCity())
                            .stopLatitude(point.stopLatitude())
                            .stopLongitude(point.stopLongitude())
                            .build())
                    .collect(Collectors.toList());


            response.getData().setRoutePoints(routePoints);
        }
        sLog.info("[ROUTE-DETAIL] Fetch Route Detail Response: {}", response);

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(FETCH_PATH)
    public ResponseEntity<FetchRouteResponse> fetchRoutes(@RequestParam(defaultValue = "1") int pageNumber,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          HttpServletRequest servletRequest) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchRoutesResult result = routeManagementService.fetchRoutes(FetchRoutesQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .merchantId(merchantId)
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .build());

        FetchRouteResponse response = FetchRouteResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchRouteResponse.FetchRouteResponsePage.builder()
                        .items(result.items().stream()
                                .map(routeResponseMapper::toFetchRouteResponseData)
                                .toList())
                        .pagination(FetchRouteResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @PostMapping(UPDATE_PATH)
    public ResponseEntity<UpdateRouteResponse> updateRoute(@Valid @RequestBody UpdateRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[UPDATE-ROUTE] Update Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        List<UpdateRouteCommand.UpdateRoutePointCommand> routePointCommandList = null;
        if (request.getData().getRoutePoints() != null) {
            routePointCommandList = request.getData().getRoutePoints().stream().map(
                    point -> UpdateRouteCommand.UpdateRoutePointCommand.builder()
                            .id(point.getId())
                            .operationOrder(point.getOperationOrder())
                            .note(point.getNote())
                            .build()
            ).toList();
        }

        UpdateRouteResult result = routeManagementService.updateRoute(UpdateRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .routeId(request.getRouteId())
                .creator(request.getCreator())
                .originName(request.getData().getOriginName())
                .destinationName(request.getData().getDestinationName())
                .status(request.getData().getStatus())
                .routePoints(routePointCommandList)
                .build());

        UpdateRouteResponse response = UpdateRouteResponse.builder()
                .routeId(result.routeId())
                .creator(result.creator())
                .data(UpdateRouteResponse.UpdateRouteResponseData.builder()
                        .originCode(result.originCode())
                        .originName(result.originName())
                        .destinationCode(result.destinationCode())
                        .destinationName(result.destinationName())
                        .status(result.status())
                        .routePoints(result.routePoints() == null ? null : result.routePoints().stream().map(
                                point -> UpdateRouteResponse.UpdateRoutePointResponse.builder()
                                        .id(point.id())
                                        .operationOrder(point.operationOrder())
                                        .note(point.note())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build())
                .build();

        sLog.info("[UPDATE-ROUTE] Update Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(CREATE_PATH)
    public ResponseEntity<CreateRouteResponse> createRoute(@Valid @RequestBody CreateRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[CREATE-ROUTE] Create Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        List<RoutePointCommand> routePointCommands = new ArrayList<>();
        if (request.getData().getOperationPoints() != null) {
            routePointCommands = request.getData().getOperationPoints().stream()
                    .map(point -> RoutePointCommand.builder()
                            .operationOrder(point.getOperationOrder())
                            .note(point.getNote())
                            .operationPointId(point.getOperationPointId())
                            .stopName(point.getStopName())
                            .stopAddress(point.getStopAddress())
                            .stopCity(point.getStopCity())
                            .stopLatitude(point.getStopLatitude())
                            .stopLongitude(point.getStopLongitude())
                            .build())
                    .toList();
        }

        CreateRouteResult result = routeManagementService.createRoute(CreateRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .originName(request.getData().getOriginName())
                .destinationName(request.getData().getDestinationName())
                .routePoints(routePointCommands)
                .build());


        List<CreateRouteRequest.RoutePoints> routePointResponses = new ArrayList<>();
        if (result.routePoints() != null) {
            routePointResponses = result.routePoints().stream()
                    .map(point -> {
                        CreateRouteRequest.RoutePoints rp = new CreateRouteRequest.RoutePoints();
                        rp.setOperationOrder(point.operationOrder());
                        rp.setNote(point.note());
                        rp.setOperationPointId(point.operationPointId());
                        rp.setStopName(point.stopName());
                        rp.setStopAddress(point.stopAddress());
                        rp.setStopCity(point.stopCity());
                        rp.setStopLatitude(point.stopLatitude());
                        rp.setStopLongitude(point.stopLongitude());
                        return rp;
                    })
                    .toList();
        }

        CreateRouteResponse response = CreateRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(CreateRouteResponse.CreateRouteResponseData.builder()
                        .id(result.id())
                        .creator(result.creator())
                        .originCode(result.originCode())
                        .destinationCode(result.destinationCode())
                        .originName(result.originName())
                        .destinationName(result.destinationName())
                        .status(result.status())
                        .routePoints(routePointResponses)
                        .build())
                .build();

        sLog.info("[CREATE-ROUTE] Create Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(DELETE_PATH)
    public ResponseEntity<DeleteRouteResponse> deleteRoute(@Valid @RequestBody DeleteRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[DELETE-ROUTE] Delete Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        DeleteRouteResult result = routeManagementService.deleteRoute(DeleteRouteCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .creator(request.getData().getCreator())
                .routeId(request.getData().getRouteId())
                .merchantId(merchantId)
                .build());


        DeleteRouteResponse response = DeleteRouteResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteRouteResponse.DeleteRouteResponseData.builder()
                        .creator(result.creator())
                        .routeId(result.routeId())
                        .status(result.status())
                        .updatedAt(result.updatedAt())
                        .build())
                .build();

        sLog.info("[DELETE-ROUTE] Delete Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }
}
