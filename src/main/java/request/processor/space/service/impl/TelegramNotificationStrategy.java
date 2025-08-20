package request.processor.space.service.impl;

import org.springframework.stereotype.Service;
import request.processor.space.constant.EnumType;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.model.request.RequestNotifications;
import request.processor.space.service.NotificationStrategy;

@Service
public class TelegramNotificationStrategy implements NotificationStrategy {

    @Override
    public EnumType getType() {
        return EnumType.TG_MESSAGE;
    }

    @Override
    public NotificationOutboxEntity prepare(RequestNotifications request) {
        return null;
    }
}
