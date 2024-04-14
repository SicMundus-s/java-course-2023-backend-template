package edu.java.scrapper.service;

import edu.java.core.dto.RequestLinkUpdate;
import edu.java.core.dto.ResponseLinkUpdate;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<ResponseLinkUpdate> sendNotification(RequestLinkUpdate update);
}
