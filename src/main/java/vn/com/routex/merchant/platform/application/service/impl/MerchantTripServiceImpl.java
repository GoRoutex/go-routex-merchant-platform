package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListResult;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripResult;
import vn.com.routex.merchant.platform.application.service.MerchantTripService;
import vn.com.routex.merchant.platform.domain.trip.port.TripAggregateRepositoryPort;

@Service
@RequiredArgsConstructor
public class MerchantTripServiceImpl implements MerchantTripService {

    private final TripAggregateRepositoryPort tripAggregateRepositoryPort;

    @Override
    public CreateTripResult createTrip(CreateTripCommand command) {
        return null;
    }

    @Override
    public UpdateTripResult updateTrip(UpdateTripCommand command) {
        return null;
    }

    @Override
    public FetchTripDetailResult fetchDetail(FetchTripDetailQuery query) {
        return null;
    }

    @Override
    public FetchTripListResult fetchTripList(FetchTripListQuery query) {
        return null;
    }
}
