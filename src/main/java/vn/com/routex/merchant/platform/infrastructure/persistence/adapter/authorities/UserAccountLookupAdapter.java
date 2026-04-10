package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.authorities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.authorities.model.UserAccountReference;
import vn.com.routex.merchant.platform.domain.authorities.port.UserAccountLookupPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.user.repository.UserJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAccountLookupAdapter implements UserAccountLookupPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserAccountReference> findById(String userId) {
        return userJpaRepository.findById(userId)
                .map(user -> new UserAccountReference(user.getId()));
    }

    @Override
    public Optional<UserAccountReference> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(user -> new UserAccountReference(user.getId()));
    }
}
