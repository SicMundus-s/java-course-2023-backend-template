package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    private final ApplicationConfig config;

    public ClientConfiguration(ApplicationConfig config) {
        this.config = config;
    }

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder webClientBuilder) {
        WebClient gitHubWebClient = webClientBuilder
            .baseUrl(config.baseUrlClient().github())
            .build();
        return new GitHubClient(gitHubWebClient);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(WebClient.Builder webClientBuilder) {
        WebClient stackOverflowWebClient = webClientBuilder
            .baseUrl(config.baseUrlClient().stackoverflow())
            .build();
        return new StackOverflowClient(stackOverflowWebClient);
    }

    @Bean
    public BotClient botClient(WebClient.Builder webClientBuilder) {
        WebClient botWebClient = webClientBuilder
            .baseUrl(config.baseUrlClient().bot())
            .build();
        return new BotClient(botWebClient);
    }
}
