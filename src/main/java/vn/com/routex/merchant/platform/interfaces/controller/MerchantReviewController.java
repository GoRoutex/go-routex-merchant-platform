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
import vn.com.routex.merchant.platform.application.command.review.CreateMerchantReviewCommand;
import vn.com.routex.merchant.platform.application.command.review.CreateMerchantReviewResult;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewDetailQuery;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewDetailResult;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewsQuery;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewsResult;
import vn.com.routex.merchant.platform.application.service.MerchantReviewService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.review.CreateMerchantReviewRequest;
import vn.com.routex.merchant.platform.interfaces.model.review.CreateMerchantReviewResponse;
import vn.com.routex.merchant.platform.interfaces.model.review.FetchMerchantReviewDetailResponse;
import vn.com.routex.merchant.platform.interfaces.model.review.FetchMerchantReviewsResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.REVIEWS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
public class MerchantReviewController {

    private final MerchantReviewService merchantReviewService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @PostMapping(REVIEWS_PATH + CREATE_PATH)
    public ResponseEntity<CreateMerchantReviewResponse> createReview(
            @Valid @RequestBody CreateMerchantReviewRequest request
    ) {
        sLog.info("[MERCHANT-REVIEW] Create Review Request: {}", request);
        CreateMerchantReviewRequest.CreateMerchantReviewRequestData data = request.getData();

        CreateMerchantReviewResult result = merchantReviewService.createReview(CreateMerchantReviewCommand.builder()
                .context(HttpUtils.toContext(request, data.getMerchantId()))
                .merchantId(data.getMerchantId())
                .reviewType(data.getReviewType())
                .bookingId(data.getBookingId())
                .routeId(data.getRouteId())
                .routeCode(data.getRouteCode())
                .driverId(data.getDriverId())
                .vehicleId(data.getVehicleId())
                .customerId(data.getCustomerId())
                .customerName(data.getCustomerName())
                .overallRating(data.getOverallRating())
                .driverRating(data.getDriverRating())
                .vehicleRating(data.getVehicleRating())
                .punctualityRating(data.getPunctualityRating())
                .tripExperienceRating(data.getTripExperienceRating())
                .safetyRating(data.getSafetyRating())
                .merchantServiceRating(data.getMerchantServiceRating())
                .staffSupportRating(data.getStaffSupportRating())
                .valueForMoneyRating(data.getValueForMoneyRating())
                .comment(data.getComment())
                .reviewedAt(data.getReviewedAt())
                .creator(data.getCreator())
                .build());

        CreateMerchantReviewResponse response = CreateMerchantReviewResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(CreateMerchantReviewResponse.CreateMerchantReviewResponseData.builder()
                        .id(result.id())
                        .merchantId(result.merchantId())
                        .reviewType(result.reviewType())
                        .bookingId(result.bookingId())
                        .routeId(result.routeId())
                        .routeCode(result.routeCode())
                        .driverId(result.driverId())
                        .vehicleId(result.vehicleId())
                        .customerId(result.customerId())
                        .customerName(result.customerName())
                        .overallRating(result.overallRating())
                        .driverRating(result.driverRating())
                        .vehicleRating(result.vehicleRating())
                        .punctualityRating(result.punctualityRating())
                        .tripExperienceRating(result.tripExperienceRating())
                        .safetyRating(result.safetyRating())
                        .merchantServiceRating(result.merchantServiceRating())
                        .staffSupportRating(result.staffSupportRating())
                        .valueForMoneyRating(result.valueForMoneyRating())
                        .comment(result.comment())
                        .reviewedAt(result.reviewedAt())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(REVIEWS_PATH + FETCH_PATH)
    @PreAuthorize("hasAuthority('reviews:management') or hasRole('ADMIN')")
    public ResponseEntity<FetchMerchantReviewsResponse> fetchReviews(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchMerchantReviewsResult result = merchantReviewService.fetchReviews(FetchMerchantReviewsQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .merchantId(merchantId)
                .pageNumber(String.valueOf(pageNumber))
                .pageSize(String.valueOf(pageSize))
                .build());

        List<FetchMerchantReviewsResponse.FetchMerchantReviewResponseData> items = result.items().stream()
                .map(item -> FetchMerchantReviewsResponse.FetchMerchantReviewResponseData.builder()
                        .id(item.id())
                        .reviewType(item.reviewType())
                        .bookingId(item.bookingId())
                        .routeId(item.routeId())
                        .routeCode(item.routeCode())
                        .driverId(item.driverId())
                        .vehicleId(item.vehicleId())
                        .customerId(item.customerId())
                        .customerName(item.customerName())
                        .overallRating(item.overallRating())
                        .driverRating(item.driverRating())
                        .vehicleRating(item.vehicleRating())
                        .punctualityRating(item.punctualityRating())
                        .tripExperienceRating(item.tripExperienceRating())
                        .safetyRating(item.safetyRating())
                        .merchantServiceRating(item.merchantServiceRating())
                        .staffSupportRating(item.staffSupportRating())
                        .valueForMoneyRating(item.valueForMoneyRating())
                        .comment(item.comment())
                        .reviewedAt(item.reviewedAt())
                        .build())
                .collect(Collectors.toList());

        FetchMerchantReviewsResponse response = FetchMerchantReviewsResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchMerchantReviewsResponse.FetchMerchantReviewsPage.builder()
                        .items(items)
                        .summary(FetchMerchantReviewsResponse.Summary.builder()
                                .totalReviews(result.totalReviews())
                                .averageOverallRating(result.averageOverallRating())
                                .build())
                        .pagination(FetchMerchantReviewsResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(REVIEWS_PATH + DETAIL_PATH)
    @PreAuthorize("hasAuthority('reviews:management') or hasRole('ADMIN')")
    public ResponseEntity<FetchMerchantReviewDetailResponse> fetchReviewDetail(
            @RequestParam String reviewId,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchMerchantReviewDetailResult result = merchantReviewService.fetchReviewDetail(FetchMerchantReviewDetailQuery.builder()
                .context(HttpUtils.toContext(baseRequest, merchantId))
                .merchantId(merchantId)
                .reviewId(reviewId)
                .build());

        FetchMerchantReviewDetailResponse response = FetchMerchantReviewDetailResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchMerchantReviewDetailResponse.FetchMerchantReviewDetailResponseData.builder()
                        .id(result.id())
                        .merchantId(result.merchantId())
                        .reviewType(result.reviewType())
                        .bookingId(result.bookingId())
                        .routeId(result.routeId())
                        .routeCode(result.routeCode())
                        .driverId(result.driverId())
                        .vehicleId(result.vehicleId())
                        .customerId(result.customerId())
                        .customerName(result.customerName())
                        .overallRating(result.overallRating())
                        .driverRating(result.driverRating())
                        .vehicleRating(result.vehicleRating())
                        .punctualityRating(result.punctualityRating())
                        .tripExperienceRating(result.tripExperienceRating())
                        .safetyRating(result.safetyRating())
                        .merchantServiceRating(result.merchantServiceRating())
                        .staffSupportRating(result.staffSupportRating())
                        .valueForMoneyRating(result.valueForMoneyRating())
                        .comment(result.comment())
                        .reviewedAt(result.reviewedAt())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
