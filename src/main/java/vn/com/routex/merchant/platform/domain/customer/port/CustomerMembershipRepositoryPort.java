package vn.com.routex.merchant.platform.domain.customer.port;


import vn.com.routex.merchant.platform.domain.customer.model.CustomerMembership;

import java.util.Optional;

public interface CustomerMembershipRepositoryPort {
    Optional<CustomerMembership> findById(String id);

    CustomerMembership save(CustomerMembership customerMembership);
}
