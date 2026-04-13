package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.repository.MerchantEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantRepositoryRepositoryAdapter implements MerchantRepositoryPort {

    private final MerchantEntityRepository merchantEntityRepository;
    private final MerchantPersistenceMapper merchantPersistenceMapper;

    @Override
    public Merchant save(Merchant merchant) {
        MerchantEntity savedEntity = merchantEntityRepository.save(merchantPersistenceMapper.toEntity(merchant));
        return merchantPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Merchant> findById(String merchantId) {
        return merchantEntityRepository.findById(merchantId)
                .map(merchantPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return merchantEntityRepository.existsByCode(code);
    }

    @Override
    public String generateMerchantCode() {
        return merchantEntityRepository.generateMerchantcode();
    }

}
