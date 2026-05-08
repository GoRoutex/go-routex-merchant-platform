package vn.com.routex.merchant.platform.domain.trip.port;

import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.trip.TripStatus;
import vn.com.routex.merchant.platform.domain.trip.model.TripAggregate;

import java.util.Optional;

public interface TripAggregateRepositoryPort {

    String generateTripCode(String originCode, String destinationCode);

    boolean existsByRouteId(String routeId, String merchantId);

    Optional<TripAggregate> findById(String tripId);

    Optional<TripAggregate> findById(String tripId, String merchantId);

    Optional<TripAggregate> findByRouteId(String routeId, String merchantId);

    void save(TripAggregate aggregate);

    PagedResult<TripAggregate> fetch(String merchantId, int pageNumber, int pageSize);

    PagedResult<TripAggregate> fetch(String merchantId, TripStatus status, int pageNumber, int pageSize);

}
