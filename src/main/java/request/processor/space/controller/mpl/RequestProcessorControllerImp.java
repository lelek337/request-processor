package request.processor.space.controller.mpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import request.processor.space.controller.RequestProcessorController;
import request.processor.space.model.request.RequestNotifications;
import request.processor.space.service.RequestProcessorService;

@Controller
@RequiredArgsConstructor
public class RequestProcessorControllerImp implements RequestProcessorController {

    private final RequestProcessorService service;

    @Override
    public void createMessage(RequestNotifications request) {
        service.processMessage(request);
    }
}
