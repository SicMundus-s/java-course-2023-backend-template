package edu.java.bot.configuration;

import edu.java.core.entity.enums.RetryBackoffStrategy;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

public class RetryPolicyConfig {

    private static final Integer MAX_ATTEMPTS = 5;
    private static final Integer INITIAL_DELAY = 1;
    private static final Integer MAX_DELAY = 10;

    public static Retry createRetryPolicy(
        RetryBackoffStrategy strategy,
        List<Integer> retryStatusCodes
    ) {
        Predicate<Throwable> retryOnStatusCodes = e ->
            e instanceof WebClientResponseException webClientResponseException &&
                retryStatusCodes.contains((webClientResponseException).getStatusCode().value());

        return createRetryPolicy(
            strategy,
            MAX_ATTEMPTS,
            Duration.ofSeconds(INITIAL_DELAY),
            Duration.ofSeconds(MAX_DELAY),
            retryOnStatusCodes
        );
    }

    private static Retry createRetryPolicy(
        RetryBackoffStrategy strategy,
        long maxAttempts,
        Duration firstBackoff,
        Duration maxBackoff,
        Predicate<Throwable> retryOnThrowable
    ) {
        return switch (strategy) {
            case CONSTANT -> Retry.fixedDelay(maxAttempts, firstBackoff)
                .filter(retryOnThrowable);
            case LINEAR -> Retry.backoff(maxAttempts, firstBackoff)
                .maxBackoff(maxBackoff)
                .filter(retryOnThrowable);
            case EXPONENTIAL -> Retry.backoff(maxAttempts, firstBackoff)
                .maxBackoff(maxBackoff)
                .jitter(0.5)
                .filter(retryOnThrowable);
        };
    }
}
