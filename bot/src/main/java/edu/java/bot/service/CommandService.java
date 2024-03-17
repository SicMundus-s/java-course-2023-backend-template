package edu.java.bot.service;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import reactor.core.publisher.Mono;

public interface CommandService {
    Mono<ResponseChat> register(Long id);

    Mono<LinkResponse> addLink(Long chatId, AddLinkRequest addLinkRequest);

    Mono<LinkResponse> removeLink(Long id, RemoveLinkRequest removeLinkRequest);

    Mono<ListLinksResponse> getLinks(Long chatId);
}
