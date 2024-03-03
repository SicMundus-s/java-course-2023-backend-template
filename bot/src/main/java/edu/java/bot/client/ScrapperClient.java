package edu.java.bot.client;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.net.URI;

public class ScrapperClient {

    private final WebClient webClient;
    public ScrapperClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public Mono<ResponseChat> registerChat(Long chatId) {
        return webClient.post()
            .uri("/v0/chats/tg-chat/{id}", chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class);
    }

    public Mono<ResponseChat> deleteChat(Long chatId) {
        return webClient.delete()
            .uri("/v0/chats/tg-chat/{id}", chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class);
    }

    public Mono<LinkResponse> addLink(Long chatId, String uri) {
        return this.webClient.post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(new AddLinkRequest(uri))
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<ListLinksResponse> getLinks(Long chatId) {
        return webClient.get()
            .uri("/v0/chats/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri("/v0/chats/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri("/v0/chats/links")
            .header("Tg-Chat-Id", chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
