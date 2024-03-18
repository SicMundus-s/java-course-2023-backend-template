package edu.java.scrapper.service.impl.jpa;

import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dao.jpa.JpaChatDao;
import edu.java.scrapper.entity.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JpaChatServiceImplTest extends IntegrationEnvironment {

    @Autowired
    private JpaChatDao jpaChatDao;

    private JpaChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        chatService = new JpaChatServiceImpl(jpaChatDao);
    }

    @Test
    @Transactional
    @Rollback
    void whenRegisterNewChat_thenItShouldBeRegistered() {
        long chatId = 123L;
        chatService.register(chatId);

        Chat foundChat = chatService.findChatByChatId(chatId);
        assertNotNull(foundChat);
        assertEquals(chatId, foundChat.getChatId());
    }

    @Test
    @Transactional
    void whenUnregisterExistingChat_thenItShouldBeUnregistered() {
        long chatId = 456L;
        chatService.register(chatId);
        chatService.unregister(chatId);

        assertThrows(NotFoundException.class, () -> chatService.findChatByChatId(chatId));
    }

    @Test
    @Transactional
    void whenFindChatByChatId_thenItShouldReturnChat() {
        long chatId = 789L;
        chatService.register(chatId);

        Chat foundChat = chatService.findChatByChatId(chatId);
        assertNotNull(foundChat);
        assertEquals(chatId, foundChat.getChatId());
    }
}
