package edu.java.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.Bot;
import edu.java.bot.service.NotificationProcessor;
import edu.java.core.dto.RequestLinkUpdate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationProcessor {

    private final Bot bot;

    @Override
    public void processNotification(RequestLinkUpdate requestLinkUpdate) {
        List<Long> chatIds = requestLinkUpdate.tgChatIds();

        chatIds.forEach(chatId -> {
            SendMessage sendMessage = new SendMessage(chatId, requestLinkUpdate.description());
            bot.execute(sendMessage);
        });
    }
}
