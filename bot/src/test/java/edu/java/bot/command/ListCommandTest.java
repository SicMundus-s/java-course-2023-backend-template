package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.comand.ListCommand;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import java.net.URI;
import java.util.List;
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
class ListCommandTest {

    @Mock
    private CommandService commandService;

    @InjectMocks
    private ListCommand listCommand;

    @Test
    void handleReturnsListOfLinksSuccessfully() {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        List<LinkResponse> links = List.of(
            new LinkResponse(1L, URI.create("http://example1.com")),
            new LinkResponse(2L, URI.create("http://example2.com"))
        );
        ListLinksResponse linksResponse = new ListLinksResponse(links, links.size());
        when(commandService.getLinks(chatId)).thenReturn(Mono.just(linksResponse));

        SendMessage result = listCommand.handle(update);

        String contentType = result.toWebhookResponse();
        assertEquals(
            "{\"chat_id\":123,\"text\":\"http://example1.com\\nhttp://example2.com\\n\",\"method\":\"sendMessage\"}",
            contentType
        );
    }
}
