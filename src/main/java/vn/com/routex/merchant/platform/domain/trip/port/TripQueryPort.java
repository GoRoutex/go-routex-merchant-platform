package vn.com.routex.merchant.platform.domain.trip.port;


import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.trip.readmodel.TripFetchView;
import vn.com.routex.merchant.platform.domain.trip.readmodel.TripSearchView;

import java.util.List;

public interface TripQueryPort {
    List<TripSearchView> searchAssignedTrips(
            String merchantId,
            String origin,
            String destination,
            int pageNumber,
            int pageSize
    );

    PagedResult<TripFetchView> fetchTrips(String merchantId, String merchantName, int pageNumber, int pageSize);

}
