package edu.java.scrapper.service.impl;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.StackOverflowQuestionResponse;
import edu.java.scrapper.dto.StackOverflowResponse;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StackOverflowServiceTest {

    @Mock
    private StackOverflowClient stackOverflowClient;

    @Mock
    private BotClient botClient;

    @Mock
    private ChatService jdbcChatServiceImpl;

    @Mock
    private LinkService jdbcLinkServiceImpl;

    @Mock
    private LinkMapper linkMapper;

    @InjectMocks
    private StackOverflowService stackOverflowService;

    @Test
    void updateWithNotification() {
        Link link = new Link();
        link.setId(1L);
        link.setUrl("https://stackoverflow.com/questions/12345678/");
        link.setUpdatedAt(OffsetDateTime.now().minusDays(2));
        link.setStackoverflowLastEditDateQuestion(OffsetDateTime.now().minusDays(2));

        StackOverflowQuestionResponse questionResponse = new StackOverflowQuestionResponse(
            Instant.now().getEpochSecond(),
            Instant.now().getEpochSecond()
        );
        StackOverflowResponse response = new StackOverflowResponse(List.of(questionResponse));
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setChatId(12345L);
        List<Chat> chats = List.of(chat);

        when(stackOverflowClient.fetchQuestionInfo(anyString())).thenReturn(Mono.just(response));
        when(jdbcChatServiceImpl.findAllChatsByLinkId(anyLong())).thenReturn(chats);
        when(linkMapper.toDtoUpdate(any(Link.class), anyList(), anyString())).thenReturn(new RequestLinkUpdate(
            1L,
            "url",
            "description",
            List.of(12345L)
        ));
        when(botClient.sendUpdate(any(RequestLinkUpdate.class)))
            .thenReturn(Mono.just(new ResponseLinkUpdate("Success")));
        doNothing().when(jdbcLinkServiceImpl).updateLink(any(Link.class));

        stackOverflowService.update(link);

        verify(stackOverflowClient, times(1)).fetchQuestionInfo("12345678");
        verify(jdbcChatServiceImpl, times(1)).findAllChatsByLinkId(1L);
        verify(jdbcLinkServiceImpl, times(1)).updateLink(link);
        verify(botClient, times(1)).sendUpdate(any(RequestLinkUpdate.class));
    }
}
