package request.processor.space.service;

import request.processor.space.constant.EnumType;
import request.processor.space.model.entity.NotificationOutboxEntity;
import request.processor.space.model.request.RequestNotifications;

public interface NotificationStrategy {

    EnumType getType();
    NotificationOutboxEntity prepare(RequestNotifications request);
}
