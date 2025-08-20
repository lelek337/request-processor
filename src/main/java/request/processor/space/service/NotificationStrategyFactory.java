package request.processor.space.service;

import org.springframework.stereotype.Service;
import request.processor.space.constant.EnumType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationStrategyFactory {

    private final Map<EnumType, NotificationStrategy> strategies = new EnumMap<>(EnumType.class);

    public NotificationStrategyFactory(List<NotificationStrategy> strategiesList) {
        strategiesList.forEach(s -> strategies.put(s.getType(), s));
    }

    public NotificationStrategy get(EnumType type) {
        return strategies.get(type);
    }
}
