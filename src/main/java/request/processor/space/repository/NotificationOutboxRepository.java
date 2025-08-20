package request.processor.space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import request.processor.space.model.entity.NotificationOutboxEntity;

import java.util.List;
import java.util.UUID;

public interface NotificationOutboxRepository extends JpaRepository<NotificationOutboxEntity, UUID> {
    List<NotificationOutboxEntity> findBySentFalseOrderByCreatedAtAsc(org.springframework.data.domain.Pageable pageable);;
}
