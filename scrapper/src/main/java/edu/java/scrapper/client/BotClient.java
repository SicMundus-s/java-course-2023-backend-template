package edu.java.scrapper.client;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {

    private final WebClient webClient;
    public BotClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public Mono<ResponseLinkUpdate> sendUpdate(RequestLinkUpdate requestLinkUpdate) {
        return webClient.post()
            .uri("/updates")
            .bodyValue(requestLinkUpdate)
            .retrieve()
            .bodyToMono(ResponseLinkUpdate.class);
    }
}
