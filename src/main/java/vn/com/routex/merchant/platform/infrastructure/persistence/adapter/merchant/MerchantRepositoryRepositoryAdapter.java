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

    @Override
    public Merchant save(Merchant merchant) {
        MerchantEntity savedEntity = merchantEntityRepository.save(toEntity(merchant));
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Merchant> findById(String merchantId) {
        return merchantEntityRepository.findById(merchantId)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return merchantEntityRepository.existsByCode(code);
    }

    @Override
    public String generateMerchantCode() {
        return merchantEntityRepository.generateMerchantcode();
    }

    private MerchantEntity toEntity(Merchant merchant) {
        return MerchantEntity.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .name(merchant.getName())
                .taxCode(merchant.getTaxCode())
                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .address(merchant.getAddress())
                .representativeName(merchant.getRepresentativeName())
                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())
                .createdAt(merchant.getCreatedAt())
                .createdBy(merchant.getCreatedBy())
                .updatedAt(merchant.getUpdatedAt())
                .updatedBy(merchant.getUpdatedBy())
                .build();
    }

    private Merchant toDomain(MerchantEntity entity) {
        return Merchant.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .taxCode(entity.getTaxCode())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .representativeName(entity.getRepresentativeName())
                .commissionRate(entity.getCommissionRate())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
