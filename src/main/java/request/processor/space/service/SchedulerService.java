package request.processor.space.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class SchedulerService {

    private final NotificationOutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${scheduler.batch-size}")
    private int batchSize;

    @Value("${scheduler.send-timeout-m}")
    private long sendTimout;

    @Scheduled(fixedDelayString = "${scheduled.ms}")
    public void process() {
        List<NotificationOutboxEntity> list = repository.findTopNBySentFalseOrderByCreatedAtAsc(batchSize);

        if (list.isEmpty()) return;

        for(NotificationOutboxEntity entity : list) {
            try {
                kafkaTemplate
                        .send(entity.getTopic(), entity.getKey(), entity.getValue())
                        .get(sendTimout, TimeUnit.MICROSECONDS);
                entity.setSent(true);
            } catch (Exception ex) {
                entity.setAttempt(entity.getAttempt() + 1);
            }
        }

        savBatchSafely(list);
    }

    @Transactional
    protected void savBatchSafely(List<NotificationOutboxEntity> list) {
        repository.saveAll(list);
    }
}
