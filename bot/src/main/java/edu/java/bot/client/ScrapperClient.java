package edu.java.bot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.configuration.RetryPolicyConfig;
import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.ListLinksResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.core.dto.ResponseChat;
import edu.java.core.exception.ApiErrorResponse;
import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import static edu.java.core.entity.enums.RetryBackoffStrategy.EXPONENTIAL;

@Slf4j
public class ScrapperClient {

    private final WebClient webClient;
    private static final String TH_CHAT_ID = "Tg-Chat-Id";
    private static final String CHAT_PATH_API = "/tg-chat/{id}";
    private static final String LINKS_PATH_API = "/links";
    private static final String LOG_ERROR = "Error processing response body: {}";
    private static final String ERROR_MESSAGE = "Error processing response body: ";
    private static final List<Integer> RETRY_CODES_LIST = List.of(500, 502, 503, 504);

    public ScrapperClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseChat> registerChat(Long chatId) {
        return webClient.post()
            .uri(CHAT_PATH_API, chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ))
            .onErrorResume(WebClientResponseException.class, ex -> {
                String responseBody = ex.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ApiErrorResponse errorResponse = objectMapper.readValue(responseBody, ApiErrorResponse.class);
                    return Mono.error(new BadRequestException(errorResponse.exceptionMessage()));
                } catch (JsonProcessingException e) {
                    log.error(LOG_ERROR, responseBody);
                    return Mono.error(new RuntimeException(ERROR_MESSAGE + responseBody));
                }
            });
    }

    public Mono<ResponseChat> deleteChat(Long chatId) {
        return webClient.delete()
            .uri(CHAT_PATH_API, chatId)
            .retrieve()
            .bodyToMono(ResponseChat.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ));
    }

    public Mono<ListLinksResponse> getLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ))
            .onErrorResume(WebClientResponseException.class, ex -> {
                String responseBody = ex.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ApiErrorResponse errorResponse = objectMapper.readValue(responseBody, ApiErrorResponse.class);
                    return Mono.error(new NotFoundException(errorResponse.exceptionMessage()));
                } catch (JsonProcessingException e) {
                    log.error(LOG_ERROR, responseBody);
                    return Mono.error(new RuntimeException(ERROR_MESSAGE + responseBody));
                }
            });
    }

    public Mono<LinkResponse> addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, String.valueOf(chatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ))
            .onErrorResume(WebClientResponseException.class, ex -> {
                String responseBody = ex.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ApiErrorResponse errorResponse = objectMapper.readValue(responseBody, ApiErrorResponse.class);
                    return Mono.error(new BadRequestException(errorResponse.exceptionMessage()));
                } catch (JsonProcessingException e) {
                    log.error(LOG_ERROR, responseBody);
                    return Mono.error(new RuntimeException(ERROR_MESSAGE + responseBody));
                }
            });
    }

    public Mono<LinkResponse> deleteLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_PATH_API)
            .header(TH_CHAT_ID, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .retryWhen(RetryPolicyConfig.createRetryPolicy(
                EXPONENTIAL,
                RETRY_CODES_LIST
            ))
            .onErrorResume(WebClientResponseException.class, ex -> {
                String responseBody = ex.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    ApiErrorResponse errorResponse = objectMapper.readValue(responseBody, ApiErrorResponse.class);
                    return Mono.error(
                        Objects.equals(errorResponse.code(), String.valueOf(HttpStatus.BAD_REQUEST.value()))
                            ? new BadRequestException(errorResponse.exceptionMessage())
                            : new NotFoundException(errorResponse.exceptionMessage()));
                } catch (JsonProcessingException e) {
                    log.error(LOG_ERROR, responseBody);
                    return Mono.error(new RuntimeException(ERROR_MESSAGE + responseBody));
                }
            });
    }
}
