package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.seat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.seat.entity.SeatTemplateEntity;

@Repository
public interface SeatTemplateEntityRepository extends JpaRepository<SeatTemplateEntity, String> {
}
