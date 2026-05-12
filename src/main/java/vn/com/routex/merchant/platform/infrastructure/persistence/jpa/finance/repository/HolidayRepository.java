package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.finance.entity.HolidayEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, String> {
    Optional<HolidayEntity> findByHolidayDate(LocalDate date);
}
