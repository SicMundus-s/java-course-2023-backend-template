package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.dto.GitHubRepositoryResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class GitHubClientTest {

    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8089")
            .build();

        gitHubClient = new GitHubClient(webClient);

        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchRepositoryInfoTest() {
        wireMockServer.stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"pushed_at\": \"2023-01-01T12:34:56Z\"}")));

        Mono<GitHubRepositoryResponse> responseMono = gitHubClient.fetchRepositoryInfo("owner", "repo");

        StepVerifier.create(responseMono)
            .expectNextMatches(response ->
                response.pushedAt().equals(OffsetDateTime.of(2023, 1,
                    1, 12, 34, 56, 0, ZoneOffset.UTC
                )))
            .verifyComplete();
    }
}
