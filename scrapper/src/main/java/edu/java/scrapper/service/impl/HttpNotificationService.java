package edu.java.scrapper.service.impl;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.service.NotificationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HttpNotificationService implements NotificationService {

    private final BotClient botClient;

    @Override
    public Mono<ResponseLinkUpdate> sendNotification(RequestLinkUpdate update) {
        return botClient.sendUpdate(update);
    }
}
