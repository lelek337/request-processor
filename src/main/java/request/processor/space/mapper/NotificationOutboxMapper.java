package request.processor.space.mapper;

import org.mapstruct.Mapper;
import request.processor.space.model.dto.NotificationOutboxDto;
import request.processor.space.model.entity.NotificationOutboxEntity;

@Mapper(componentModel = "spring")
public interface NotificationOutboxMapper {

    NotificationOutboxDto toDto(NotificationOutboxEntity entity);
}
