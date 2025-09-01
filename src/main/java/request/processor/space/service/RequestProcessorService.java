package request.processor.space.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import request.processor.space.mapper.NotificationOutboxMapper;
import request.processor.space.model.dto.NotificationOutboxDto;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.model.request.RequestNotifications;
import request.processor.space.repository.NotificationOutboxRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class RequestProcessorService {

    private final NotificationOutboxRepository repository;
    private final NotificationStrategyFactory strategyFactory;
    private final NotificationOutboxMapper mapper;

    public NotificationOutboxDto processMessage(RequestNotifications request) {

        NotificationOutboxEntity outbox = strategyFactory.get(request.getType()).prepare(request);
        log.info("outbox: {}", outbox);
        NotificationOutboxEntity notificationOutbox = repository.save(outbox);
        NotificationOutboxDto notificationOutboxDto = mapper.toDto(notificationOutbox);

        log.info("Подготовлено сообщение для отправки. Key: {}, Payload: {}, topic: {}", outbox.getKey(), outbox.getValue(), outbox.getTopic());

        return notificationOutboxDto;
    }
}
