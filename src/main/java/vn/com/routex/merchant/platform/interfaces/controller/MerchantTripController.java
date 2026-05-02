package vn.com.routex.merchant.platform.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripResult;
import vn.com.routex.merchant.platform.application.service.MerchantTripService;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.model.trip.CreateTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.CreateTripResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.DeleteTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.DeleteTripResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.FetchTripDetailResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.FetchTripListResponse;
import vn.com.routex.merchant.platform.interfaces.model.trip.UpdateTripRequest;
import vn.com.routex.merchant.platform.interfaces.model.trip.UpdateTripResponse;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.TRIPS_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
public class MerchantTripController {

    private final MerchantTripService merchantTripService;

    @PostMapping(TRIPS_PATH + CREATE_PATH)
    public ResponseEntity<CreateTripResponse> createTrip(@Valid @RequestBody CreateTripRequest request,
                                                         HttpServletRequest servletRequest) {

        String merchantId = ApiRequestUtils.getMerchantId(servletRequest);

        CreateTripResult result = merchantTripService.createTrip(CreateTripCommand.builder()
                        .context(HttpUtils.toContext(request))
                        .routeId(request.getData().getRouteId())
                        .merchantId(merchantId)
                        .departureTime(request.getData().getDepartureTime())
                        .rawDepartureDate(request.getData().getRawDepartureDate())
                        .rawDepartureTime(request.getData().getRawDepartureTime())
                        .durationMinutes(request.getData().getDurationMinutes())
                .build());

        CreateTripResponse response;
        return null;
    }

    public ResponseEntity<UpdateTripResponse> updateTrip(@Valid @RequestBody UpdateTripRequest request) {
        return null;
    }

    public ResponseEntity<DeleteTripResponse> deleteTrip(@Valid @RequestBody DeleteTripRequest request) {
        return null;
    }

    public ResponseEntity<FetchTripDetailResponse> fetchDetail(
            @RequestParam String tripId,
            @RequestParam(required = false) TripStatus status,
            HttpServletRequest request
    ) {
        return null;
    }
    public ResponseEntity<FetchTripListResponse> fetchList(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            HttpServletRequest servletRequest
    ) {
        return null;
    }
}
