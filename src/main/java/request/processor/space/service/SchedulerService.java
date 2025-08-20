package request.processor.space.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.repository.NotificationOutboxRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class SchedulerService {

    private final NotificationOutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${scheduler.batch-size}")
    private int batchSize;

    @Value("${scheduler.timeout-ms}")
    private long timeoutMs;

    @Scheduled( cron = "${scheduler.cron}")
    public void process() {
        log.info("Старт SchedulerService");
        List<NotificationOutboxEntity> list = fetchBatch(batchSize);

        if (list.isEmpty()) {
            log.debug("Нет записей для отправки в kafka");
            return;
        }

        for(NotificationOutboxEntity entity : list) {
            try {
                kafkaTemplate
                        .send(entity.getTopic(), entity.getKey(), entity.getValue())
                        .get(timeoutMs, TimeUnit.MICROSECONDS);
                entity.setSent(true);
            } catch (Exception ex) {
                log.warn("Отправка не удалась. topic={} key={} attempt={} error={}",
                        entity.getTopic(), entity.getKey(), entity.getAttempt(), ex.toString());
                entity.setAttempt(entity.getAttempt() + 1);
            }
        }

        savBatchSafely(list);
        log.info("SchedulerService: обработка завершена (успешно={}, всего={})",
                list.stream().filter(NotificationOutboxEntity::isSent).count(), list.size());
    }

    public List<NotificationOutboxEntity> fetchBatch(int batchSize) {
        return repository.findBySentFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize));
    }

    @Transactional
    protected void savBatchSafely(List<NotificationOutboxEntity> list) {
        repository.saveAll(list);
    }
}
