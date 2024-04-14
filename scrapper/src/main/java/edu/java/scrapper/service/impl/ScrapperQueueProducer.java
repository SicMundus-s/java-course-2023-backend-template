package edu.java.scrapper.service.impl;

import edu.java.core.dto.RequestLinkUpdate;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    public CompletableFuture<SendResult<String, Object>> send(RequestLinkUpdate update) {
        return kafkaTemplate.send(topic, update);
    }
}
