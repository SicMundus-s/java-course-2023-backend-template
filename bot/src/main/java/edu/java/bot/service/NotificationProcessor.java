package edu.java.bot.service;

import edu.java.core.dto.RequestLinkUpdate;

public interface NotificationProcessor {
    void processNotification(RequestLinkUpdate requestLinkUpdate);
}
