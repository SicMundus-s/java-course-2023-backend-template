package edu.java.scrapper.service.impl.jpa;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.dao.jpa.JpaChatDao;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatServiceImpl implements ChatService {

    private final JpaChatDao jpaChatDao;

    @Override
    @Transactional
    public void register(long chatId) {
        if (jpaChatDao.findByChatId(chatId).isPresent()) {
            throw new BadRequestException("Chat is already registered.");
        }

        Chat chat = new Chat();
        chat.setChatId(chatId);
        jpaChatDao.save(chat);
    }

    @Override
    @Transactional
    public void unregister(long chatId) {
        Chat chat = jpaChatDao.findByChatId(chatId)
            .orElseThrow(() -> new NotFoundException("Chat with ID: " + chatId + " does not exist."));
        jpaChatDao.delete(chat);
    }

    @Override
    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jpaChatDao.findAllByLinkId(linkId);
    }

    @Override
    public Chat findChatByChatId(Long chatId) {
        return jpaChatDao.findByChatId(chatId)
            .orElseThrow(() -> new NotFoundException("Chat with ID: " + chatId + " not found."));
    }
}
