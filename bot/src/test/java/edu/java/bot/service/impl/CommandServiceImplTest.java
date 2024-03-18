package edu.java.bot.service.impl;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.handler.ResourceHandler;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandServiceImplTest {

    @Mock
    private List<ResourceHandler> handlers;

    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private CommandServiceImpl commandService;


    @Test
    void testRegisterSuccess() {
        Long chatId = 1L;
        ResponseChat expectedResponse = new ResponseChat("Registered");
        when(scrapperClient.registerChat(chatId)).thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(commandService.register(chatId))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void testRemoveLinkSuccess() {
        Long chatId = 1L;
        RemoveLinkRequest request = new RemoveLinkRequest("https://example.com");
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("https://example.com"));

        when(scrapperClient.deleteLink(chatId, request)).thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(commandService.removeLink(chatId, request))
            .expectNext(expectedResponse)
            .verifyComplete();
    }

    @Test
    void testGetLinksSuccess() {
        Long chatId = 1L;
        ListLinksResponse expectedResponse =
            new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("https://example.com"))), 1);

        when(scrapperClient.getLinks(chatId)).thenReturn(Mono.just(expectedResponse));

        StepVerifier.create(commandService.getLinks(chatId))
            .expectNext(expectedResponse)
            .verifyComplete();
    }
}
