package edu.java.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.Bot;
import edu.java.core.dto.RequestLinkUpdate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private final Bot bot;

    public void updateProcess(RequestLinkUpdate requestLinkUpdate) {
        List<Long> chatIds = requestLinkUpdate.tgChatIds();

        chatIds.forEach(chatId -> {
            SendMessage sendMessage = new SendMessage(chatId, requestLinkUpdate.description());
            bot.execute(sendMessage);
        });
    }

}
