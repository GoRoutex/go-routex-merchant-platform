package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.merchant.ApplicationFormStatus;
import vn.com.routex.merchant.platform.domain.merchant.model.Merchant;
import vn.com.routex.merchant.platform.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.merchant.platform.interfaces.model.internal.merchant.InternalUpdateMerchantRequest;

import java.util.List;

public interface InternalMerchantAdminService {

    Merchant fetchMerchantDetail(String merchantId, RequestContext context);

    PagedResult<Merchant> fetchMerchants(RequestContext context, int pageNumber, int pageSize);

    PagedResult<Merchant> fetchMerchants(RequestContext context, String merchantName, int pageNumber, int pageSize);

    List<Merchant> fetchMerchantsByIds(List<String> merchantIds, RequestContext context);

    List<String> findMerchantIdsByName(String merchantName, RequestContext context);

    Merchant updateMerchant(InternalUpdateMerchantRequest request, RequestContext context);

    PagedResult<MerchantApplicationForm> fetchApplicationForms(RequestContext context, ApplicationFormStatus status, int pageNumber, int pageSize);

    MerchantApplicationForm fetchApplicationFormDetail(String applicationFormId, RequestContext context);
}
