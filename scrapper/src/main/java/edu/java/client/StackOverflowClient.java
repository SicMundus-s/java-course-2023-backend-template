package edu.java.client;

import edu.java.dto.StackOverflowResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private final WebClient webClient;

    public StackOverflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<StackOverflowResponse> fetchQuestionInfo(String questionId) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{id}")
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow")
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class);
    }
}
