package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.UntrackCommand;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.RemoveLinkRequest;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UntrackCommandTest {
    @Mock
    private CommandService commandService;

    @InjectMocks
    private UntrackCommand untrackCommand;

    @Test
    void untrackCommandHandlesUrlSuccessfully() {
        Long chatId = 123L;
        Update update = mockUpdateWithMessage("/untrack http://example.com", chatId);

        when(commandService.removeLink(eq(chatId), any(RemoveLinkRequest.class)))
            .thenReturn(Mono.just(new LinkResponse(1L, URI.create("http://example.com"))));

        SendMessage result = untrackCommand.handle(update);

        String resultJson = result.toWebhookResponse();
        assertEquals(
            "{\"chat_id\":123,\"text\":\"Untracking: http://example.com\",\"method\":\"sendMessage\"}",
            resultJson
        );
    }

    @Test
    void untrackCommandHandlesMissingUrl() {
        Long chatId = 123L;
        Update update = mockUpdateWithMessage("/untrack", chatId);

        SendMessage result = untrackCommand.handle(update);

        String resultJson = result.toWebhookResponse();
        assertEquals(
            "{\"chat_id\":123,\"text\":\"Usage: /untrack \\u003cURL\\u003e\",\"method\":\"sendMessage\"}",
            resultJson
        );
    }

    private Update mockUpdateWithMessage(String text, Long chatId) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);
        return update;
    }
}
