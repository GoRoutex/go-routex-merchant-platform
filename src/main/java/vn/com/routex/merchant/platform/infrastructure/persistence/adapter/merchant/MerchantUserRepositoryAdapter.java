package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantUser;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantUserRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.repository.MerchantUserEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantUserRepositoryAdapter implements MerchantUserRepositoryPort {

    private final MerchantUserEntityRepository merchantUserEntityRepository;
    private final MerchantUserPersistenceMapper merchantUserPersistenceMapper;

    @Override
    public MerchantUser save(MerchantUser merchantUser) {
        return merchantUserPersistenceMapper.toDomain(
                merchantUserEntityRepository.save(merchantUserPersistenceMapper.toEntity(merchantUser))
        );
    }

    @Override
    public Optional<MerchantUser> findByUserId(String userId) {
        return merchantUserEntityRepository.findByUserId(userId)
                .map(merchantUserPersistenceMapper::toDomain);
    }
}
