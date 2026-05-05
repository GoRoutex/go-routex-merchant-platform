package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.common.RequestContext;
import vn.com.routex.merchant.platform.interfaces.model.internal.booking.InternalBookingContextResponses;

public interface InternalBookingContextService {

    InternalBookingContextResponses.TripBookingContextData fetchTripBookingContext(String tripId, RequestContext context);

    InternalBookingContextResponses.VehicleSeatBlueprintData fetchVehicleSeatBlueprint(String vehicleId, RequestContext context);
}
