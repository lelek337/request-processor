package request.processor.space.controller.mpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import request.processor.space.controller.RequestProcessorController;
import request.processor.space.model.dto.NotificationOutboxDto;
import request.processor.space.model.request.RequestNotifications;
import request.processor.space.service.RequestProcessorService;

@RestController
@RequiredArgsConstructor
public class RequestProcessorControllerImp implements RequestProcessorController {

    private final RequestProcessorService service;

    @Override
    public ResponseEntity<NotificationOutboxDto> createMessage(RequestNotifications request) {
        NotificationOutboxDto response =  service.processMessage(request);

        return ResponseEntity.ok(response);
    }
}
