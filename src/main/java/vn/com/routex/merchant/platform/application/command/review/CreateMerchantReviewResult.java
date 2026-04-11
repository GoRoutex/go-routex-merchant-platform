package vn.com.routex.merchant.platform.application.command.review;

import lombok.Builder;
import vn.com.routex.merchant.platform.domain.review.ReviewType;

@Builder
public record CreateMerchantReviewResult(
        String id,
        String merchantId,
        ReviewType reviewType,
        String bookingId,
        String routeId,
        String routeCode,
        String driverId,
        String vehicleId,
        String customerId,
        String customerName,
        Integer overallRating,
        Integer driverRating,
        Integer vehicleRating,
        Integer punctualityRating,
        Integer tripExperienceRating,
        Integer safetyRating,
        Integer merchantServiceRating,
        Integer staffSupportRating,
        Integer valueForMoneyRating,
        String comment,
        String reviewedAt
) {
}
