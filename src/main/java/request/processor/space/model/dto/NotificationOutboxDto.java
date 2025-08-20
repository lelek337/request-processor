package request.processor.space.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class NotificationOutboxDto {

    private UUID uuid;
    private LocalDateTime createdAt;
    private String topic;
    private String value;
    private boolean sent;
    private int attempt;

}
