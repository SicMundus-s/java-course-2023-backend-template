package edu.java.bot.listner;

import edu.java.bot.service.impl.MessageService;
import edu.java.bot.service.impl.NotificationService;
import edu.java.core.dto.RequestLinkUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final NotificationService notificationService;
    private final MessageService messageService;

    @KafkaListener(topics = "${kafka.scrapper-topic.name}")
    public void listen(RequestLinkUpdate update) {
        messageService.process();
        notificationService.processNotification(update);
    }
}
