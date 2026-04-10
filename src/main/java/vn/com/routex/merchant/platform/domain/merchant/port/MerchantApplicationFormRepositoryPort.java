package vn.com.routex.merchant.platform.domain.merchant.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantApplicationForm;

import java.util.Optional;

public interface MerchantApplicationFormRepositoryPort {

    MerchantApplicationForm save(MerchantApplicationForm merchantApplicationForm);

    boolean existsByFormCode(String formCode);

    String generateFormCode();

    Optional<MerchantApplicationForm> findById(String id);

    PagedResult<MerchantApplicationForm> fetch(int pageNumber, int pageSize);
}
