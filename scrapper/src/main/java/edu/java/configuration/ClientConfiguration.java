package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    private static final String DEFAULT_GITHUB_BASE_URL = "https://api.github.com";
    private static final String DEFAULT_STACKOVERFLOW_BASE_URL = "https://api.stackexchange.com/2.3";

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder webClientBuilder) {
        WebClient gitHubWebClient = webClientBuilder
            .baseUrl(DEFAULT_GITHUB_BASE_URL)
            .build();
        return new GitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient.Builder webClientBuilder) {
        WebClient stackOverflowWebClient = webClientBuilder
            .baseUrl(DEFAULT_STACKOVERFLOW_BASE_URL)
            .build();
        return new StackOverflowClient(stackOverflowWebClient);
    }
}
