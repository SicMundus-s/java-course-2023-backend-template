package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.StartCommand;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.ResponseChat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private CommandService commandService;

    @InjectMocks
    private StartCommand startCommand;

    @Test
    void handleReturnsWelcomeMessageSuccessfully() {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        String welcomeMessage = "Welcome to the bot!";
        when(commandService.register(chatId)).thenReturn(Mono.just(new ResponseChat(welcomeMessage)));

        SendMessage result = startCommand.handle(update);

        String resultJson = result.toWebhookResponse();
        assertEquals("{\"chat_id\":123,\"text\":\"Welcome to the bot!\",\"method\":\"sendMessage\"}", resultJson);
    }

    @Test
    void handleReturnsErrorMessageOnFailure() {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        when(commandService.register(chatId)).thenReturn(Mono.error(new RuntimeException("Registration failed")));

        SendMessage result = startCommand.handle(update);

        String expectedJsonError =
            "{\"chat_id\":123,\"text\":\"Something went wrong. Please try again later.\",\"method\":\"sendMessage\"}";
        String resultJson = result.toWebhookResponse();
        assertEquals(expectedJsonError, resultJson);
    }
}
