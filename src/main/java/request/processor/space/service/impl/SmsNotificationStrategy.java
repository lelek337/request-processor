package request.processor.space.service.impl;

import org.springframework.stereotype.Service;
import request.processor.space.constant.EnumType;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.model.request.RequestNotifications;
import request.processor.space.service.NotificationStrategy;
import request.processor.space.util.NotificationOutboxUtil;

@Service
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public EnumType getType() {
        return EnumType.SMS;
    }

    @Override
    public NotificationOutboxEntity prepare(RequestNotifications request) {
        return NotificationOutboxUtil.build(getType(), request);
    }
}
