package edu.java.scrapper.service.impl;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.dto.GitHubRepositoryResponse;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.mapper.LinkMapper;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private BotClient botClient;

    @Mock
    private ChatService jdbcChatServiceImpl;

    @Mock
    private LinkService jdbcLinkServiceImpl;

    @Mock
    private LinkMapper linkMapper;

    @InjectMocks
    private GitHubService gitHubService;

    @Test
    void updateWithNotification() {
        Link link = new Link();
        link.setId(1L);
        link.setUrl("https://github.com/owner/repo");
        link.setUpdatedAt(OffsetDateTime.now().minusDays(2));
        link.setGithubUpdatedAt(OffsetDateTime.now().minusDays(2));
        GitHubRepositoryResponse response = new GitHubRepositoryResponse(
            OffsetDateTime.now(), OffsetDateTime.now());
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setChatId(12345L);
        List<Chat> chats = List.of(chat);

        when(gitHubClient.fetchRepositoryInfo(anyString(), anyString())).thenReturn(Mono.just(response));
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

        gitHubService.update(link);

        verify(gitHubClient, times(1)).fetchRepositoryInfo("owner", "repo");
        verify(jdbcChatServiceImpl, times(1)).findAllChatsByLinkId(1L);
        verify(jdbcLinkServiceImpl, times(1)).updateLink(link);
        verify(botClient, times(1)).sendUpdate(any(RequestLinkUpdate.class));
    }
}
