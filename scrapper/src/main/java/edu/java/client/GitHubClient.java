package edu.java.client;

import edu.java.dto.GitHubRepositoryResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<GitHubRepositoryResponse> fetchRepositoryInfo(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class);
    }
}
