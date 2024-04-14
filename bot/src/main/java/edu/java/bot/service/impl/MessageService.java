package edu.java.bot.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    private final Counter messageCounter;

    public MessageService(MeterRegistry meterRegistry) {
        this.messageCounter = Counter.builder("processed_messages")
            .description("The number of processed messages")
            .tags("service", "message_service")
            .register(meterRegistry);
    }

    public void process() {
        messageCounter.increment();
    }
}
