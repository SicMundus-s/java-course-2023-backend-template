package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ApplicationConfig config;

    @Bean
    public ScrapperClient scrapperClient(WebClient.Builder webClientBuilder) {
        String scrapper = config.scrapper();
        WebClient gitHubWebClient = webClientBuilder
            .baseUrl(scrapper)
            .build();
        return new ScrapperClient(gitHubWebClient);
    }
}
