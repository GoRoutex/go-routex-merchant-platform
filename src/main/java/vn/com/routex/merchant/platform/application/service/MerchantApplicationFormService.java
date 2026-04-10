package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.AcceptMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.RejectMerchantApplicationResult;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationCommand;
import vn.com.routex.merchant.platform.application.command.merchant.SubmitMerchantApplicationResult;

public interface MerchantApplicationFormService {

    SubmitMerchantApplicationResult submit(SubmitMerchantApplicationCommand command);

    AcceptMerchantApplicationResult accept(AcceptMerchantApplicationCommand command);

    RejectMerchantApplicationResult reject(RejectMerchantApplicationCommand command);
}
