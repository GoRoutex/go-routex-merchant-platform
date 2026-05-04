package vn.com.routex.merchant.platform.domain.merchant.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;

import java.util.Optional;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    Optional<Merchant> findById(String merchantId);

    boolean existsByCode(String code);

    String generateMerchantCode();

    PagedResult<Merchant> fetch(int pageNumber, int pageSize);

    PagedResult<Merchant> fetch(String merchantName, int pageNumber, int pageSize);

    java.util.List<Merchant> findByIds(java.util.List<String> merchantIds);

    java.util.List<String> findIdsByMerchantName(String merchantName);
}
