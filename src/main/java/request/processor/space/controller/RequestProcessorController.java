package request.processor.space.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import request.processor.space.model.dto.NotificationOutboxDto;
import request.processor.space.model.request.RequestNotifications;

import static request.processor.space.constant.ApiConstant.BASE_API;

@RequestMapping(BASE_API)
public interface RequestProcessorController {

    @Operation(
            summary = "Создание сообщения для передачи в kafka",
            description = "Пзволяет создать сообщение в БД и kafka"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешное создание сообщения"),
            @ApiResponse(responseCode = "400", description = "Неправельные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
            @PostMapping
    ResponseEntity<NotificationOutboxDto> createMessage(@Valid @RequestBody RequestNotifications request);
}
