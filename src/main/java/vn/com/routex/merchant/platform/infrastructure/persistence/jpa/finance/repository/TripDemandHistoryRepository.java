package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.TripDemandHistoryEntity;

@Repository
public interface TripDemandHistoryRepository extends JpaRepository<TripDemandHistoryEntity, String> {
}
