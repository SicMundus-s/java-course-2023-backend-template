package edu.java.scrapper.service.impl.jooq;

import edu.java.scrapper.dao.jooq.JooqChatDao;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqChatServiceImpl implements ChatService {

    private final JooqChatDao jooqChatDao;

    @Override
    public void register(long chatId) {
        jooqChatDao.register(chatId);
    }

    @Override
    public void unregister(long chatId) {
        jooqChatDao.removeChat(chatId);
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jooqChatDao.findAllChatsByLinkId(linkId);
    }

    @Override
    public Chat findChatByChatId(Long chatId) {
        return jooqChatDao.findChatByChatId(chatId);
    }
}
