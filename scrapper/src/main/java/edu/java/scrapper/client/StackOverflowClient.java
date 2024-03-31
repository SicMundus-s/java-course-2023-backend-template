package edu.java.scrapper.client;

import edu.java.scrapper.configuration.RetryPolicyConfig;
import edu.java.scrapper.dto.StackOverflowResponse;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static edu.java.core.entity.enums.RetryBackoffStrategy.EXPONENTIAL;

public class StackOverflowClient {

    private static final List<Integer> RETRY_CODES_LIST = List.of(500, 502, 503, 504);
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
            .bodyToMono(StackOverflowResponse.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ));
    }
}
