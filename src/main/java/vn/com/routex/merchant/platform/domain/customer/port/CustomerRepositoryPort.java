package vn.com.routex.merchant.platform.domain.customer.port;


import vn.com.routex.merchant.platform.domain.customer.model.Customer;

import java.util.Optional;

public interface CustomerRepositoryPort {

    Optional<Customer> findByUserId(String userId);
    Customer save(Customer customer);
}
