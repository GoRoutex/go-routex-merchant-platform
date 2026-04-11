package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.review.ReviewType;
import vn.com.routex.merchant.platform.domain.review.model.MerchantReview;
import vn.com.routex.merchant.platform.domain.review.port.MerchantReviewRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.review.entity.MerchantReviewEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.review.repository.MerchantReviewEntityRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MerchantReviewRepositoryAdapter implements MerchantReviewRepositoryPort {

    private final MerchantReviewEntityRepository merchantReviewEntityRepository;
    private final MerchantReviewPersistenceMapper merchantReviewPersistenceMapper;

    @Override
    public MerchantReview save(MerchantReview merchantReview) {
        MerchantReviewEntity entity = merchantReviewEntityRepository.save(merchantReviewPersistenceMapper.toEntity(merchantReview));
        return merchantReviewPersistenceMapper.toDomain(entity);
    }

    @Override
    public PagedResult<MerchantReview> fetchByMerchantId(String merchantId, int pageNumber, int pageSize) {
        Page<MerchantReviewEntity> page = merchantReviewEntityRepository.findByMerchantIdOrderByReviewedAtDesc(
                merchantId,
                PageRequest.of(pageNumber, pageSize)
        );

        List<MerchantReview> items = page.getContent().stream()
                .map(merchantReviewPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<MerchantReview>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public long countByMerchantId(String merchantId) {
        return merchantReviewEntityRepository.countByMerchantId(merchantId);
    }

    @Override
    public Double findAverageOverallRatingByMerchantId(String merchantId) {
        return merchantReviewEntityRepository.findAverageOverallRatingByMerchantId(merchantId);
    }

    @Override
    public boolean existsTripReview(String merchantId, String bookingId, String customerId) {
        return merchantReviewEntityRepository.existsByMerchantIdAndBookingIdAndCustomerIdAndReviewType(
                merchantId,
                bookingId,
                customerId,
                ReviewType.TRIP
        );
    }
}
