package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.review.CreateMerchantReviewCommand;
import vn.com.routex.merchant.platform.application.command.review.CreateMerchantReviewResult;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewsQuery;
import vn.com.routex.merchant.platform.application.command.review.FetchMerchantReviewsResult;

public interface MerchantReviewService {

    CreateMerchantReviewResult createReview(CreateMerchantReviewCommand command);

    FetchMerchantReviewsResult fetchReviews(FetchMerchantReviewsQuery query);
}
