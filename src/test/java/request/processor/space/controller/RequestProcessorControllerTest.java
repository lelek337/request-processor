package request.processor.space.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import request.processor.space.constant.ApiConstant;
import request.processor.space.constant.EnumType;
import request.processor.space.model.request.RequestNotifications;
import support.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RequestProcessorControllerTest extends BaseIntegrationTest {

    @Test
    @DisplayName("POST /messages должен вернуть 200 OK и тело NotificationOutboxDto")
    void testCreateMessage() throws Exception {
        // given
        RequestNotifications request = new RequestNotifications();
        request.setType(EnumType.EMAIL);
        request.setMessage("test message");

        // when & then
        mockMvc.perform(post(ApiConstant.BASE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @DisplayName("POST /messages без обязательных полей должен вернуть 400 Bad Request")
    void testCreateMessageBadRequest() throws Exception {
        // given
        RequestNotifications request = new RequestNotifications();

        // when & then
        mockMvc.perform(post(ApiConstant.BASE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
