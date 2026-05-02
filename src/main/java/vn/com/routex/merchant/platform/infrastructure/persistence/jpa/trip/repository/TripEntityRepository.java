package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.trip.entity.TripEntity;

@Repository
public interface TripEntityRepository extends JpaRepository<TripEntity, String> {
}
