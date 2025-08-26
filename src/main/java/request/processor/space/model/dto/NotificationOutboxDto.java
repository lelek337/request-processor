package request.processor.space.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationOutboxDto {

    private UUID id;
    private LocalDateTime createdAt;
    private String topic;
    private String key;
    private String value;
    private boolean sent;
    private int attempt;
}
