package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.customer;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.customer.model.Customer;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.customer.entity.CustomerEntity;

@Component
public class CustomerPersistenceMapper {

    public CustomerEntity toJpaEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .status(customer.getStatus())
                .fullName(customer.getFullName())
                .createdAt(customer.getCreatedAt())
                .createdBy(customer.getCreatedBy())
                .totalTrips(customer.getTotalTrips())
                .totalSpent(customer.getTotalSpent())
                .updatedAt(customer.getUpdatedAt())
                .updatedBy(customer.getUpdatedBy())
                .lastBookingAt(customer.getLastBookingAt())
                .lastTripAt(customer.getLastTripAt())
                .build();
    }

    public Customer toDomain(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(customerEntity.getId())
                .userId(customerEntity.getUserId())
                .status(customerEntity.getStatus())
                .fullName(customerEntity.getFullName())
                .createdAt(customerEntity.getCreatedAt())
                .createdBy(customerEntity.getCreatedBy())
                .updatedAt(customerEntity.getUpdatedAt())
                .updatedBy(customerEntity.getUpdatedBy())
                .lastBookingAt(customerEntity.getLastBookingAt())
                .lastTripAt(customerEntity.getLastTripAt())
                .build();
    }
}
