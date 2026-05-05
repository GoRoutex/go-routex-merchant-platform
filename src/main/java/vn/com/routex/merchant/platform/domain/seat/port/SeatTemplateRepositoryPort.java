package vn.com.routex.merchant.platform.domain.seat.port;

import vn.com.routex.merchant.platform.domain.seat.SeatFloor;
import vn.com.routex.merchant.platform.domain.seat.model.SeatTemplate;

import java.util.List;

public interface SeatTemplateRepositoryPort {

    List<SeatTemplate> findByVehicleTemplateId(String vehicleTemplateId);

    List<SeatTemplate> findByVehicleTemplateIdAndFloor(String vehicleTemplateId, SeatFloor floor);
}
