package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.core.dto.AddLinkRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class ScrapperClientTest {

    private WireMockServer wireMockServer;
    private ScrapperClient scrapperClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());

        WebClient webClient = WebClient.builder()
            .baseUrl(wireMockServer.baseUrl())
            .build();

        scrapperClient = new ScrapperClient(webClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getLinks_Success() {
        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"links\":[{\"url\":\"http://example.com\"}]}")
                .withStatus(200)));

        StepVerifier.create(scrapperClient.getLinks(1L))
            .expectNextMatches(response -> response.links().size() == 1 &&
                "http://example.com".equals(response.links().getFirst().url().toString()))
            .verifyComplete();
    }

    @Test
    void addLink_Success() {
        String requestBody = "{\"link\":\"http://example.com\", \"resourceType\":null}";
        String responseBody = "{\"url\":\"http://example.com\", \"description\":\"Link added\"}";

        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson(requestBody, true, true))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(responseBody)
                .withStatus(200)));

        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink("http://example.com");

        StepVerifier.create(scrapperClient.addLink(1L, addLinkRequest))
            .expectNextMatches(linkResponse -> "http://example.com".equals(linkResponse.url().toString()))
            .verifyComplete();
    }

    @Test
    void registerChat_Success() {
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"response\":\"Chat registered\"}")
                .withStatus(200)));

        StepVerifier.create(scrapperClient.registerChat(1L))
            .expectNextMatches(response -> "Chat registered".equals(response.response()))
            .verifyComplete();
    }
}
