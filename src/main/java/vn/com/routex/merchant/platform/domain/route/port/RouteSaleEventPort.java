package vn.com.routex.merchant.platform.domain.route.port;


import vn.com.routex.merchant.platform.infrastructure.kafka.event.TripSellableEvent;

public interface RouteSaleEventPort {
    void publishRouteReadyForSale(
            String requestId,
            String requestDateTime,
            String channel,
            String aggregateId,
            TripSellableEvent payload
    );
}
