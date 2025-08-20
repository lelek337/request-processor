package request.processor.space.model.request;

import lombok.Data;
import request.processor.space.constant.EnumType;

@Data
public class RequestNotifications {

    private EnumType type;
    private String message;
}
