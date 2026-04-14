package vn.com.routex.merchant.platform.domain.merchant.port;

import vn.com.routex.merchant.platform.domain.merchant.model.MerchantUser;

import java.util.Optional;

public interface MerchantUserRepositoryPort {

    MerchantUser save(MerchantUser merchantUser);

    Optional<MerchantUser> findByUserId(String userId);
}
