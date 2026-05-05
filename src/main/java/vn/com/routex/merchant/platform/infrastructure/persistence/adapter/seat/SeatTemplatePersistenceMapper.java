package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.seat;

import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.seat.model.SeatTemplate;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.seat.entity.SeatTemplateEntity;

@Component
public class SeatTemplatePersistenceMapper {

    public SeatTemplate toDomain(SeatTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        return SeatTemplate.builder()
                .id(entity.getId())
                .vehicleTemplateId(entity.getVehicleTemplateId())
                .seatCode(entity.getSeatCode())
                .floor(entity.getFloor())
                .rowNo(entity.getRowNo())
                .columnNo(entity.getColumnNo())
                .type(entity.getType())
                .isActive(entity.isActive())
                .build();
    }
}
