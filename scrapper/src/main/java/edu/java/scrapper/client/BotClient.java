package edu.java.scrapper.client;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import edu.java.scrapper.configuration.RetryPolicyConfig;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static edu.java.core.entity.enums.RetryBackoffStrategy.EXPONENTIAL;

public class BotClient {
    private static final List<Integer> RETRY_CODES_LIST = List.of(500, 502, 503, 504);
    private final WebClient webClient;

    public BotClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseLinkUpdate> sendUpdate(RequestLinkUpdate requestLinkUpdate) {
        return webClient.post()
            .uri("/updates")
            .bodyValue(requestLinkUpdate)
            .retrieve()
            .bodyToMono(ResponseLinkUpdate.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ));
    }
}
