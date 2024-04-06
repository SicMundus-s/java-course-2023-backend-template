package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.service.NotificationService;
import edu.java.scrapper.service.impl.HttpNotificationService;
import edu.java.scrapper.service.impl.QueueNotificationService;
import edu.java.scrapper.service.impl.ScrapperQueueProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAccessConfiguration {

    @Bean
    public NotificationService notificationService(
        ApplicationConfig applicationConfig,
        ScrapperQueueProducer scrapperQueueProducer,
        BotClient botClient
    ) {
        if (applicationConfig.useQueue()) {
            return new QueueNotificationService(scrapperQueueProducer);
        } else {
            return new HttpNotificationService(botClient);
        }
    }
}
