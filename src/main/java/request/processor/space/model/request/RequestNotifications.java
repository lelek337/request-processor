package request.processor.space.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import request.processor.space.constant.EnumType;

@Data
public class RequestNotifications {

    @NotNull
    private EnumType type;

    @NotBlank
    private String message;
}
