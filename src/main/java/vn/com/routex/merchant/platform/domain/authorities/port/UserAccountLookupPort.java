package vn.com.routex.merchant.platform.domain.authorities.port;

import vn.com.routex.merchant.platform.domain.authorities.model.UserAccountReference;

import java.util.Optional;

public interface UserAccountLookupPort {
    Optional<UserAccountReference> findById(String userId);
}
