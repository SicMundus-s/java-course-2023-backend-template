package edu.java.scrapper.client;

import edu.java.scrapper.configuration.RetryPolicyConfig;
import edu.java.scrapper.dto.GitHubRepositoryResponse;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static edu.java.core.entity.enums.RetryBackoffStrategy.EXPONENTIAL;

public class GitHubClient {

    private static final List<Integer> RETRY_CODES_LIST = List.of(500, 502, 503, 504);
    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<GitHubRepositoryResponse> fetchRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ));
    }
}
