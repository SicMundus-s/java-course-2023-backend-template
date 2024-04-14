package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "appsrap", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull @Bean Scheduler scheduler,
    @NotNull @Bean BaseUrlClient baseUrlClient,
    @NotNull AccessType databaseAccessType,
    boolean useQueue
) {

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record BaseUrlClient(@NotEmpty String github, @NotEmpty String stackoverflow, @NotEmpty String bot) {

    }
}
