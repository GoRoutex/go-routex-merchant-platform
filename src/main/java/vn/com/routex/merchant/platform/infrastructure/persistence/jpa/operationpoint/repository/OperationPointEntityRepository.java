package vn.com.routex.merchant.platform.infrastructure.persistence.jpa.operationpoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.operationpoint.entity.OperationPointEntity;

import java.util.Optional;


@Repository
public interface OperationPointEntityRepository extends JpaRepository<OperationPointEntity, String> {
    Optional<OperationPointEntity> findByCode(String code);

    boolean existsByCode(String code);
}
