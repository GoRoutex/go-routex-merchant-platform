package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.service.OutBoxService;
import vn.com.routex.merchant.platform.domain.outbox.OutBoxEventStatus;
import vn.com.routex.merchant.platform.domain.outbox.model.OutBoxEvent;
import vn.com.routex.merchant.platform.domain.outbox.port.OutBoxEventRepositoryPort;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutBoxServiceImpl implements OutBoxService {
    private final OutBoxEventRepositoryPort outboxEventRepositoryPort;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @Override
    @Transactional
    public void generateEvent(String aggregateId, String topic, String eventName, String eventKey, Object payload, BaseRequest context) {
        OutBoxEvent outboxEvent = OutBoxEvent.builder()
                .id(UUID.randomUUID().toString())
                .aggregateId(aggregateId)
                .topic(topic)
                .eventType(eventName)
                .eventKey(eventKey)
                .payload(Map.of("data", payload))
                .header(Map.of("context", context))
                .status(OutBoxEventStatus.PENDING)
                .retryCount(0)
                .availableAt(OffsetDateTime.now())
                .processedAt(null)
                .build();

        outboxEventRepositoryPort.save(outboxEvent);
        sLog.info("[OUTBOX-EVENT] Outbox event generated and queued: AggregateId={} EventId={}",
                aggregateId, outboxEvent.getId());
    }
}
