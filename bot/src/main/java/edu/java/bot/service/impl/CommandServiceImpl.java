package edu.java.bot.service.impl;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.UrlNotSupported;
import edu.java.bot.handler.ResourceHandler;
import edu.java.bot.service.CommandService;
import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import edu.java.core.entity.enums.ResourceType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import static edu.java.bot.util.UrlChecker.checkUrlExists;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {

    private final List<ResourceHandler> handlers;
    private final ScrapperClient scrapperClient;

    @Override
    public Mono<ResponseChat> register(Long id) {
        return scrapperClient.registerChat(id);
    }

    @Override
    public Mono<LinkResponse> addLink(Long chatId, AddLinkRequest addLinkRequest) {
        String link = addLinkRequest.getLink();
        ResourceType resourceType = canHandle(link);
        if (resourceType.equals(ResourceType.UNKNOWN) || !checkUrlExists(link)) {
            throw new UrlNotSupported("This URL is not supported for tracking.");
        }
        addLinkRequest.setResourceType(resourceType);
        return scrapperClient.addLink(chatId, addLinkRequest);
    }

    @Override
    public Mono<LinkResponse> removeLink(Long id, RemoveLinkRequest removeLinkRequest) {
        return scrapperClient.deleteLink(id, removeLinkRequest);
    }

    @Override
    public Mono<ListLinksResponse> getLinks(Long chatId) {
        return scrapperClient.getLinks(chatId);
    }

    private ResourceType canHandle(String url) {
        for (ResourceHandler resourceHandler : handlers) {
            if (resourceHandler.canHandle(url)) {
                return resourceHandler.getResourceType();
            }
        }
        return ResourceType.UNKNOWN;
    }
}
