package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.seat.SeatFloor;
import vn.com.routex.merchant.platform.domain.seat.model.SeatTemplate;
import vn.com.routex.merchant.platform.domain.seat.port.SeatTemplateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.seat.repository.SeatTemplateEntityRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatTemplateRepositoryAdapter implements SeatTemplateRepositoryPort {

    private final SeatTemplateEntityRepository seatTemplateEntityRepository;
    private final SeatTemplatePersistenceMapper seatTemplatePersistenceMapper;

    @Override
    public List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId) {
        return seatTemplateEntityRepository.findByVehicleTemplateId(vehicleTemplateId).stream()
                .map(seatTemplatePersistenceMapper::toDomain)
                .filter(SeatTemplate::isActive)
                .toList();
    }

    @Override
    public List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor) {
        return seatTemplateEntityRepository.findByVehicleTemplateIdAndFloor(vehicleTemplateId, floor).stream()
                .map(seatTemplatePersistenceMapper::toDomain)
                .filter(SeatTemplate::isActive)
                .toList();
    }
}
