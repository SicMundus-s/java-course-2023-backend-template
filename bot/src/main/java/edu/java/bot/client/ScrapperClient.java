package edu.java.bot.client;

import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {

    private final WebClient webClient;
    private static final String TH_CHAT_ID = "Tg-Chat-Id";
    private static final String CHAT_PATH_API = "/v0/chats/tg-chat/{id}";
    private static final String LINKS_PATH_API = "/v0/chats/links";

    public ScrapperClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public Mono<ResponseChat> registerChat(Long chatId) {
        return webClient.post()
            .uri(CHAT_PATH_API, chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class);
    }

    public Mono<ResponseChat> deleteChat(Long chatId) {
        return webClient.delete()
            .uri(CHAT_PATH_API, chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class);
    }

    public Mono<ListLinksResponse> getLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, String.valueOf(chatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
