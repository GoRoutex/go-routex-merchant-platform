package vn.com.routex.merchant.platform.application.query;


import vn.com.routex.merchant.platform.application.command.customer.CustomerMembershipView;

import java.util.Optional;

public interface CustomerMembershipQueryRepository {
    Optional<CustomerMembershipView> findMembershipSummaryByUserId(String userId);
}
