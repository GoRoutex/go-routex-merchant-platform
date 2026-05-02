package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.trip.CreateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.CreateTripResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripDetailResult;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListQuery;
import vn.com.routex.merchant.platform.application.command.trip.FetchTripListResult;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripCommand;
import vn.com.routex.merchant.platform.application.command.trip.UpdateTripResult;

public interface MerchantTripService {

    CreateTripResult createTrip(CreateTripCommand command);

    UpdateTripResult updateTrip(UpdateTripCommand command);

    FetchTripDetailResult fetchDetail(FetchTripDetailQuery query);

    FetchTripListResult fetchTripList(FetchTripListQuery query);
}
