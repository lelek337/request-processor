package request.processor.space.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumType {
    SMS("sms-events"),
    EMAIL("email-events"),
    PUSH("push-events"),
    TG_MESSAGE("telegram-events");

    private final String topic;
}
