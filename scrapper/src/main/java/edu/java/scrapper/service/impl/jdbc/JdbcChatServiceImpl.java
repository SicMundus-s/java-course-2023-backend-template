package edu.java.scrapper.service.impl.jdbc;

import edu.java.scrapper.dao.jdbc.JdbcChatDao;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcChatServiceImpl implements ChatService {

    private final JdbcChatDao jdbcChatDao;

    @Override
    public void register(long chatId) {
        jdbcChatDao.register(chatId);
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        jdbcChatDao.removeChat(chatId);
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jdbcChatDao.findAllChatsByLinkId(linkId);
    }

    @Override
    public Chat findChatByChatId(Long chatId) {
        return jdbcChatDao.findChatByChatId(chatId);
    }
}
