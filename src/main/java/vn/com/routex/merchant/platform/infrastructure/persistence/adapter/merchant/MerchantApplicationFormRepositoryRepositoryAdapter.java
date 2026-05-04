package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormStatus;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.merchant.platform.domain.merchant.port.MerchantApplicationFormRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.repository.MerchantApplicationFormEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantApplicationFormRepositoryRepositoryAdapter implements MerchantApplicationFormRepositoryPort {

    private final MerchantApplicationFormEntityRepository merchantApplicationFormEntityRepository;
    private final MerchantApplicationFormPersistenceMapper merchantApplicationFormPersistenceMapper;

    @Override
    public MerchantApplicationForm save(MerchantApplicationForm merchantApplicationForm) {
        return merchantApplicationFormPersistenceMapper.toDomain(
                merchantApplicationFormEntityRepository.save(
                        merchantApplicationFormPersistenceMapper.toEntity(merchantApplicationForm)
                )
        );
    }

    @Override
    public boolean existsByFormCode(String formCode) {
        return merchantApplicationFormEntityRepository.existsByFormCode(formCode);
    }

    @Override
    public String generateFormCode() {
        return merchantApplicationFormEntityRepository.generateFormCode();
    }

    @Override
    public Optional<MerchantApplicationForm> findById(String id) {
        return merchantApplicationFormEntityRepository.findById(id)
                .map(merchantApplicationFormPersistenceMapper::toDomain);
    }

    @Override
    public PagedResult<MerchantApplicationForm> fetch(int pageNumber, int pageSize) {
        Page<vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantApplicationFormEntity> page =
                merchantApplicationFormEntityRepository.findAll(
                        PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "submittedAt"))
                );

        return PagedResult.<MerchantApplicationForm>builder()
                .items(page.getContent().stream().map(merchantApplicationFormPersistenceMapper::toDomain).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public PagedResult<MerchantApplicationForm> fetchByStatus(ApplicationFormStatus status, int pageNumber, int pageSize) {
        Page<vn.com.routex.merchant.platform.infrastructure.persistence.jpa.merchant.entity.MerchantApplicationFormEntity> page =
                merchantApplicationFormEntityRepository.findByStatus(
                        status,
                        PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "submittedAt"))
                );

        return PagedResult.<MerchantApplicationForm>builder()
                .items(page.getContent().stream().map(merchantApplicationFormPersistenceMapper::toDomain).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
