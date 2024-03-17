package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.dto.StackOverflowResponse;
import edu.java.scrapper.mapper.StackOverflowQuestionResponseMapper;
import java.time.Instant;
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
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

class StackOverflowClientTest {

    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        stubFor(get(urlPathEqualTo("/questions/123"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"items\": [{\"last_activity_date\": 1680213422}]}")));

        WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8089")
            .build();
        stackOverflowClient = new StackOverflowClient(webClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchQuestionInfoTest() {
        Mono<StackOverflowResponse> responseMono = stackOverflowClient.fetchQuestionInfo("123");

        StackOverflowQuestionResponseMapper mapper = new StackOverflowQuestionResponseMapper();

        StepVerifier.create(responseMono)
            .expectNextMatches(response ->
                mapper.mapToOffSetDateTime(response.items().getFirst().lastActivityDate())
                    .equals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1680213422), ZoneOffset.UTC)))
            .verifyComplete();
    }
}
