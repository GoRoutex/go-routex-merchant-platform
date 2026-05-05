package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.trip;


import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.trip.model.TripAggregate;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.trip.entity.TripEntity;

@Component
public class TripAggregatePersistenceMapper {
    public TripEntity toEntity(TripAggregate aggregate) {
        if(aggregate == null) {
            return null;
        }

        return TripEntity.builder()
        		.id(aggregate.getId())
        		.routeId(aggregate.getRouteId())
        		.merchantId(aggregate.getMerchantId())
        		.pickupBranch(aggregate.getPickupBranch())
        		.tripCode(aggregate.getTripCode())
        		.departureTime(aggregate.getDepartureTime())
        		.rawDepartureTime(aggregate.getRawDepartureTime())
        		.rawDepartureDate(aggregate.getRawDepartureDate())
        		.status(aggregate.getStatus())
        		.build();
    }

    public TripAggregate toDomain(TripEntity entity) {
        if (entity == null) {
            return null;
        }
        return TripAggregate.builder()
                .id(entity.getId())
                .routeId(entity.getRouteId())
                .merchantId(entity.getMerchantId())
                .pickupBranch(entity.getPickupBranch())
                .tripCode(entity.getTripCode())
                .departureTime(entity.getDepartureTime())
                .rawDepartureTime(entity.getRawDepartureTime())
                .rawDepartureDate(entity.getRawDepartureDate())
                .status(entity.getStatus())
                .build();
    }
}
