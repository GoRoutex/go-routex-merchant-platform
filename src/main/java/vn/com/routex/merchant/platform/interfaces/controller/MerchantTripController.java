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
import vn.com.routex.merchant.platform.application.command.route.AssignRouteCommand;
import vn.com.routex.merchant.platform.application.command.route.AssignRouteResult;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripResult;
import vn.com.routex.merchant.platform.application.command.trip.DeleteTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.DeleteTripResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListResult;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripResult;
import vn.com.routex.merchant.platform.application.service.MerchantTripService;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.assignment.AssignRouteRequest;
import vn.com.routex.merchant.platform.interfaces.model.assignment.AssignRouteResponse;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.CreateTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.CreateTripResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.DeleteTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.DeleteTripResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.FetchTripDetailResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.FetchTripListResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.UpdateTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.UpdateTripResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.ASSIGNMENT_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.TRIPS_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('trip:management') or hasRole('ADMIN')")
public class MerchantTripController {

    private final MerchantTripService merchantTripService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(TRIPS_PATH + ASSIGNMENT_PATH)
    public ResponseEntity<AssignRouteResponse> assignRoute(@Valid @RequestBody AssignRouteRequest request,
                                                           HttpServletRequest servletRequest) {
        sLog.info("[ASSIGN-ROUTE] Assign Route Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        AssignRouteResult result = merchantTripService.assignRoute(AssignRouteCommand.builder()
                .merchantId(merchantId)
                .creator(request.getData().getCreator())
                .tripId(request.getData().getTripId())
                .vehicleId(request.getData().getVehicleId())
                .driverId(request.getData().getDriverId())
                .context(HttpUtils.toContext(request, merchantId))
                .build());

        AssignRouteResponse response = AssignRouteResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(AssignRouteResponse.AssignRouteResponseData.builder()
                        .creator(result.creator())
                        .tripId(result.tripId())
                        .vehicleId(result.vehicleId())
                        .assignedAt(result.assignedAt())
                        .status(result.status())
                        .build())
                .build();

        sLog.info("[ASSIGN-ROUTE] Assign Route Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(TRIPS_PATH + CREATE_PATH)
    public ResponseEntity<CreateTripResponse> createTrip(@Valid @RequestBody CreateTripRequest request,
                                                         HttpServletRequest servletRequest) {
        sLog.info("[TRIP-MANAGEMENT] Create Trip Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);

        CreateTripResult result = merchantTripService.createTrip(CreateTripCommand.builder()
                        .context(HttpUtils.toContext(request, merchantId))
                        .routeId(request.getData().getRouteId())
                        .merchantId(merchantId)
                        .pickupBranch(request.getData().getPickupBranch())
                        .departureTime(request.getData().getDepartureTime())
                        .rawDepartureDate(request.getData().getRawDepartureDate())
                        .rawDepartureTime(request.getData().getRawDepartureTime())
                        .build());

        CreateTripResponse response = CreateTripResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(CreateTripResponse.CreateTripResponseData.builder()
                        .tripId(result.tripId())
                        .routeId(result.routeId())
                        .merchantId(result.merchantId())
                        .departureTime(result.departureTime())
                        .pickupBranch(result.pickupBranch())
                        .rawDepartureTime(result.rawDepartureTime())
                        .rawDepartureDate(result.rawDepartureDate())
                        .status(result.status())
                        .build())
                .build();
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(TRIPS_PATH + UPDATE_PATH)
    public ResponseEntity<UpdateTripResponse> updateTrip(@Valid @RequestBody UpdateTripRequest request,
                                                         HttpServletRequest servletRequest) {
        sLog.info("[TRIP-MANAGEMENT] Update Trip Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);

        UpdateTripResult result = merchantTripService.updateTrip(UpdateTripCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .tripId(request.getData().getTripId())
                .routeId(request.getData().getRouteId())
                .merchantId(merchantId)
                .pickupBranch(request.getData().getPickupBranch())
                .departureTime(request.getData().getDepartureTime())
                .rawDepartureTime(request.getData().getRawDepartureTime())
                .rawDepartureDate(request.getData().getRawDepartureDate())
                .build());

        UpdateTripResponse response = UpdateTripResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateTripResponse.UpdateTripResponseData.builder()
                        .tripId(result.tripId())
                        .routeId(result.routeId())
                        .pickupBranch(result.pickupBranch())
                        .merchantId(result.merchantId())
                        .departureTime(result.departureTime())
                        .rawDepartureTime(result.rawDepartureTime())
                        .rawDepartureDate(result.rawDepartureDate())
                        .status(result.status())
                        .build())
                .build();
        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(TRIPS_PATH + DELETE_PATH)
    public ResponseEntity<DeleteTripResponse> deleteTrip(@Valid @RequestBody DeleteTripRequest request,
                                                         HttpServletRequest servletRequest) {
        sLog.info("[TRIP-MANAGEMENT] Delete Trip Request: {}", request);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);

        DeleteTripResult result = merchantTripService.deleteTrip(DeleteTripCommand.builder()
                .context(HttpUtils.toContext(request, merchantId))
                .tripId(request.getData().getTripId())
                .merchantId(merchantId)
                .build());

        DeleteTripResponse response = DeleteTripResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteTripResponse.DeleteTripResponseData.builder()
                        .tripId(result.tripId())
                        .status(result.status())
                        .build())
                .build();
        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(TRIPS_PATH + DETAIL_PATH)
    public ResponseEntity<FetchTripDetailResponse> fetchDetail(
            @RequestParam String tripId,
            @RequestParam(required = false) TripStatus status,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchTripDetailResult result = merchantTripService.fetchDetail(FetchTripDetailQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .tripId(tripId)
                .merchantId(merchantId)
                .status(status)
                .build());

        FetchTripDetailResponse response = FetchTripDetailResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchTripDetailResponse.FetchTripDetailResponseData.builder()
                        .tripId(result.tripId())
                        .merchantId(result.merchantId())
                        .pickupBranch(result.pickupBranch())
                        .tripCode(result.tripCode())
                        .departureTime(result.departureTime())
                        .rawDepartureTime(result.rawDepartureTime())
                        .rawDepartureDate(result.rawDepartureDate())
                        .status(result.status())
                        .route(FetchTripDetailResponse.FetchTripRouteData.builder()
                                .routeId(result.route().routeId())
                                .originName(result.route().originName())
                                .destinationName(result.route().destinationName())
                                .duration(result.route().duration())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(TRIPS_PATH + FETCH_PATH)
    public ResponseEntity<FetchTripListResponse> fetchList(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) TripStatus status,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchTripListResult result = merchantTripService.fetchTripList(FetchTripListQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .status(status)
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .build());

        List<FetchTripListResponse.FetchTripListResponseData> items = result.items().stream()
                .map(item -> FetchTripListResponse.FetchTripListResponseData.builder()
                            .tripId(item.tripId())
                        .merchantId(item.merchantId())
                        .tripCode(item.tripCode())
                        .pickupBranch(item.pickupBranch())
                        .departureTime(item.departureTime())
                        .rawDepartureDate(item.rawDepartureDate())
                        .rawDepartureTime(item.rawDepartureTime())
                        .status(item.status())
                        .route(FetchTripListResponse.FetchTripListRouteData.builder()
                                .routeId(item.route().routeId())
                                .originName(item.route().originName())
                                .destinationName(item.route().destinationName())
                                .duration(item.route().duration())
                                .build())
                        .build())
                .collect(Collectors.toList());

        FetchTripListResponse response = FetchTripListResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchTripListResponse.FetchTripListResponsePage.builder()
                        .items(items)
                        .pagination(FetchTripListResponse.Pagination.builder()
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
