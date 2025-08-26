package request.processor.space.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.repository.NotificationOutboxRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    NotificationOutboxRepository repository;

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Spy
    @InjectMocks
    SchedulerService service;

    private final int batchSize = 10;
    private long timeoutMs = 1000;

    @BeforeEach
    void setUp() {
        setField(service, "batchSize", batchSize);
        setField(service, "timeoutMs", timeoutMs);
    }

    @Test
    @DisplayName("Пустая строка - отправка и сохраниение не происходят")
    void emptyBatch_noSend_noSave() {
        //Given
        when(repository.findBySentFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize)))
                .thenReturn(List.of());

        //When
        service.process();

        //Then
        verifyNoInteractions(kafkaTemplate);
        verify(service, never()).savBatchSafely(anyList());
    }

    @Test
    @DisplayName("Успешная отправка сообщения")
    void testProcess_SuccessfulSend() throws Exception {
        // Given
        NotificationOutboxEntity entity = notificationOutboxEntity();
        List<NotificationOutboxEntity> list = Collections.singletonList(entity);
        when(repository.findBySentFalseOrderByCreatedAtAsc(any())).thenReturn(list);
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(null));

        // When
        service.process();

        // Then
        verify(kafkaTemplate, timeout(1)).send(entity.getTopic(), entity.getKey(), entity.getValue());
        verify(repository, times(1)).saveAll(list);
        assertTrue(entity.isSent(), "Сообщение должно быть отмечено как отправленное");
    }

    @Test
    @DisplayName("Ошибка при тправке сообщения")
    void testProcess_SendFailure() throws Exception {
        //Given
        NotificationOutboxEntity entity = notificationOutboxEntity();

        List<NotificationOutboxEntity> list = Collections.singletonList(entity);
        when(repository.findBySentFalseOrderByCreatedAtAsc(any())).thenReturn(list);
        when(kafkaTemplate.send(any(),  any(),  any())).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Kafka error")));

        //When
        service.process();

        //Then
        verify(kafkaTemplate, times(1)).send(entity.getTopic(), entity.getKey(), entity.getValue());
        verify(repository, times(1)).saveAll(list);
        assertFalse(entity.isSent());
        assertEquals(1, entity.getAttempt());
    }

    private NotificationOutboxEntity notificationOutboxEntity() {
        return NotificationOutboxEntity.builder()
                .topic("test-topic")
                .key("test-key")
                .value("test-value")
                .sent(false)
                .attempt(0)
                .build();
    }
}
