package edu.java.scrapper.service;

import edu.java.scrapper.entity.Chat;
import java.util.List;

public interface ChatService {

    void register(long chatId);

    void unregister(long chatId);

    List<Chat> findAllChatsByLinkId(Long linkId);

    Chat findChatByChatId(Long chatId);
}
