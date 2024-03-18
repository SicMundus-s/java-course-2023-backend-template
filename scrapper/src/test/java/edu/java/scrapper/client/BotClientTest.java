package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class BotClientTest {

    private WireMockServer wireMockServer;
    private BotClient botClient;

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);

        wireMockServer.stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"description\":\"Success\"}")
                .withStatus(200)));

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8089").build();
        botClient = new BotClient(webClient);
    }

    @Test
    void sendUpdateTest() {
        RequestLinkUpdate request = new RequestLinkUpdate(1L, "http://example.com", "Description", List.of(123L));

        Mono<ResponseLinkUpdate> responseMono = botClient.sendUpdate(request);

        StepVerifier.create(responseMono)
            .expectNextMatches(response -> "Success".equals(response.description()))
            .verifyComplete();
    }
}
