package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.TrackCommand;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackCommandTest {

    @Mock
    private CommandService commandService;

    @InjectMocks
    private TrackCommand trackCommand;

    @Test
    void handleSuccessfullyAddsLink() {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        when(message.text()).thenReturn("/track http://example.com");

        LinkResponse linkResponse = new LinkResponse(1L, URI.create("http://example.com"));
        when(commandService.addLink(any(Long.class), any(AddLinkRequest.class))).thenReturn(Mono.just(linkResponse));

        SendMessage result = trackCommand.handle(update);
        String resultJson = result.toWebhookResponse();
        assertEquals(
            "{\"chat_id\":123,\"text\":\"Started tracking: http://example.com\",\"method\":\"sendMessage\"}",
            resultJson
        );
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
        when(message.text()).thenReturn("/track http://example.com");

        when(commandService.addLink(
            any(Long.class),
            any(AddLinkRequest.class)
        )).thenReturn(Mono.error(new RuntimeException("Failed to add link")));

        SendMessage result = trackCommand.handle(update);
        String resultJson = result.toWebhookResponse();
        assertEquals(
            "{\"chat_id\":123,\"text\":\"Something went wrong. Please try again later.\",\"method\":\"sendMessage\"}",
            resultJson
        );
    }
}
