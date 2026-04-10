package vn.com.routex.merchant.platform.domain.merchant.port;

import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    boolean existsByCode(String code);

    String generateMerchantCode();
}
