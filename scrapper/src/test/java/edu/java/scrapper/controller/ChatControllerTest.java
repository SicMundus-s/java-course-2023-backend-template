package edu.java.scrapper.controller;

import edu.java.scrapper.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService jdbcChatServiceImpl;

    @Test
    void registerChatSuccess() throws Exception {
        Long chatId = 12345L;

        doNothing().when(jdbcChatServiceImpl).register(anyLong());

        mockMvc.perform(post("/v0/chats/tg-chat/{id}", chatId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteChatSuccess() throws Exception {
        Long chatId = 12345L;

        doNothing().when(jdbcChatServiceImpl).unregister(anyLong());

        mockMvc.perform(delete("/v0/chats/tg-chat/{id}", chatId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerChatBadRequest() throws Exception {
        mockMvc.perform(post("/v0/chats/tg-chat/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteChatBadRequest() throws Exception {
        mockMvc.perform(delete("/v0/chats/tg-chat/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
