package vn.com.routex.merchant.platform.infrastructure.persistence.adapter.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.merchant.platform.domain.assignment.TripAssignmentStatus;
import vn.com.routex.merchant.platform.domain.assignment.model.TripAssignmentRecord;
import vn.com.routex.merchant.platform.domain.assignment.port.TripAssignmentRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.adapter.route.RoutePersistenceMapper;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.assignment.entity.TripAssignmentEntity;
import vn.com.routex.merchant.platform.infrastructure.persistence.jpa.assignment.repository.TripAssignmentEntityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TripAssignmentRepositoryAdapter implements TripAssignmentRepositoryPort {

    private final TripAssignmentEntityRepository tripAssignmentEntityRepository;
    private final RoutePersistenceMapper routePersistenceMapper;

    @Override
    public boolean existsActiveByTripId(String tripId) {
        return tripAssignmentEntityRepository.existsByTripId(tripId);
    }

    @Override
    public boolean existsActiveByTripId(String TripId, String merchantId) {
        return tripAssignmentEntityRepository.existsByTripIdAndMerchantId(TripId, merchantId);
    }


    @Override
    public Optional<TripAssignmentRecord> findByTripIdAndMerchantId(String TripId, String merchantId) {
        return tripAssignmentEntityRepository.findByTripIdAndMerchantId(TripId, merchantId)
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public List<TripAssignmentRecord> findByTripIdAndMerchantId(List<String> TripIds, String merchantId) {
        return tripAssignmentEntityRepository.findByTripIdInAndMerchantId(TripIds, merchantId)
                .stream()
                .map(routePersistenceMapper::toAssignmentRecord).toList();
    }

    @Override
    public Optional<TripAssignmentRecord> findActiveByTripId(String TripId) {
        return tripAssignmentEntityRepository
                .findFirstByTripIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(TripId, TripAssignmentStatus.ASSIGNED)
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Optional<TripAssignmentRecord> findActiveByTripId(String TripId, String merchantId) {
        return tripAssignmentEntityRepository
                .findFirstByTripIdAndMerchantIdAndStatusAndUnAssignedAtIsNullOrderByAssignedAtDesc(
                        TripId,
                        merchantId,
                        TripAssignmentStatus.ASSIGNED
                )
                .map(routePersistenceMapper::toAssignmentRecord);
    }

    @Override
    public Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> TripIds) {
        List<TripAssignmentEntity> assignments = tripAssignmentEntityRepository.findActiveByTripIdsNative(TripIds, TripAssignmentStatus.ASSIGNED.name());
        return toAssignmentMap(assignments);
    }

    @Override
    public Map<String, TripAssignmentRecord> findLatestActiveByTripIds(List<String> TripIds, String merchantId) {
        List<TripAssignmentEntity> assignments = tripAssignmentEntityRepository.findActiveByTripIdsAndMerchantIdNative(
                TripIds,
                merchantId,
                TripAssignmentStatus.ASSIGNED.name()
        );
        return toAssignmentMap(assignments);
    }

    @Override
    public List<TripAssignmentRecord> findByMerchantId(String merchantId) {
        return tripAssignmentEntityRepository.findByMerchantId(merchantId).stream()
                .map(routePersistenceMapper::toAssignmentRecord)
                .toList();
    }

    private Map<String, TripAssignmentRecord> toAssignmentMap(List<TripAssignmentEntity> assignments) {
        return assignments.stream()
                .map(routePersistenceMapper::toAssignmentRecord)
                .collect(Collectors.toMap(
                        TripAssignmentRecord::getTripId,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(TripAssignmentRecord::getAssignedAt))
                ));
    }

    @Override
    public void save(TripAssignmentRecord assignment) {
        tripAssignmentEntityRepository.save(routePersistenceMapper.toEntity(assignment));
    }
}
