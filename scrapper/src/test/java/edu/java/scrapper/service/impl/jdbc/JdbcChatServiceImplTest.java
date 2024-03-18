package edu.java.scrapper.service.impl.jdbc;

import edu.java.scrapper.dao.jdbc.JdbcChatDao;
import edu.java.scrapper.entity.Chat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcChatServiceImplTest {

    @Mock
    private JdbcChatDao jdbcChatDao;

    @InjectMocks
    private JdbcChatServiceImpl chatService;

    @Test
    void registerCallsDaoRegister() {
        long chatId = 1L;
        chatService.register(chatId);

        verify(jdbcChatDao, times(1)).register(chatId);
    }

    @Test
    void unregisterCallsDaoRemoveChat() {
        long chatId = 1L;
        chatService.unregister(chatId);

        verify(jdbcChatDao, times(1)).removeChat(chatId);
    }

    @Test
    void findAllChatsByLinkIdReturnsCorrectData() {
        Long linkId = 1L;
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setChatId(1L);
        List<Chat> expectedChats = List.of(chat);

        when(jdbcChatDao.findAllChatsByLinkId(linkId)).thenReturn(expectedChats);

        List<Chat> actualChats = chatService.findAllChatsByLinkId(linkId);

        verify(jdbcChatDao, times(1)).findAllChatsByLinkId(linkId);
        assert actualChats.equals(expectedChats);
    }

    @Test
    void findChatByChatIdReturnsCorrectChat() {
        Long chatId = 1L;
        Chat expectedChat = new Chat();
        expectedChat.setId(chatId);
        expectedChat.setChatId(chatId);

        when(jdbcChatDao.findChatByChatId(chatId)).thenReturn(expectedChat);

        Chat actualChat = chatService.findChatByChatId(chatId);

        verify(jdbcChatDao, times(1)).findChatByChatId(chatId);
        assert actualChat.equals(expectedChat);
    }
}

