package edu.java.scrapper.service.impl;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import edu.java.scrapper.service.NotificationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class QueueNotificationService implements NotificationService {

    private final ScrapperQueueProducer scrapperQueueProducer;

    @Override
    public Mono<ResponseLinkUpdate> sendNotification(RequestLinkUpdate update) {
        return Mono.fromFuture(scrapperQueueProducer.send(update)).map(sendResult -> {
            String topic = sendResult.getRecordMetadata().topic();
            int partition = sendResult.getRecordMetadata().partition();
            long offset = sendResult.getRecordMetadata().offset();
            String responseDescription =
                String.format("Message sent to topic %s partition %d at offset %d", topic, partition, offset);
            return new ResponseLinkUpdate(responseDescription);
        });
    }
}
