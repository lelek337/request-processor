package request.processor.space.util;

import lombok.experimental.UtilityClass;
import request.processor.space.constant.EnumType;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.model.request.RequestNotifications;

import java.util.UUID;

@UtilityClass
public class NotificationOutboxUtil {

    public NotificationOutboxEntity build(EnumType type, RequestNotifications req) {
        return NotificationOutboxEntity.builder()
                .topic(type.getTopic())
                .key(UUID.randomUUID().toString())
                .value(req.getMessage())
                .sent(false)
                .attempt(0)
                .build();
    }
}
