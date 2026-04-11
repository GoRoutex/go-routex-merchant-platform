package vn.com.routex.merchant.platform.domain.review.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.review.model.MerchantReview;

public interface MerchantReviewRepositoryPort {

    MerchantReview save(MerchantReview merchantReview);

    PagedResult<MerchantReview> fetchByMerchantId(String merchantId, int pageNumber, int pageSize);

    long countByMerchantId(String merchantId);

    Double findAverageOverallRatingByMerchantId(String merchantId);

    boolean existsTripReview(String merchantId, String bookingId, String customerId);
}
