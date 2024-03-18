package edu.java.scrapper.dao.jdbc;

import edu.java.core.exception.BadRequestException;
import edu.java.core.exception.NotFoundException;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.mapper.rowmapper.ChatRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcChatDao {

    private final JdbcTemplate jdbcTemplate;
    private final ChatRowMapper chatRowMapper;

    private static final String SELECT_CHAT_BY_CHATID = "SELECT id, chat_id FROM chats WHERE chat_id = ?";

    public void register(Long chatId) {
        if (!isNotRegistered(chatId)) {
            throw new BadRequestException("Chat is already registered");
        }
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);
    }

    public void removeChat(Long chatId) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM chats WHERE id = ?", chatId);
        if (rowsAffected == 0) {
            throw new NotFoundException("Chat with ID: " + chatId + " does not exist");
        }
    }

    public List<Chat> findAllChatsByLinkId(Long linkId) {
        return jdbcTemplate.query("SELECT c.id, c.chat_id FROM chats c "
            + " join chat_links cl on c.id = cl.chat_id "
            + " where link_id = ? ", chatRowMapper, linkId);
    }

    public Chat findChatByChatId(Long chatId) {
        return jdbcTemplate.queryForObject(SELECT_CHAT_BY_CHATID, chatRowMapper, chatId);
    }

    public boolean isNotRegistered(Long chatId) {
        Optional<Long> saveId;
        try {
            saveId = Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT id FROM chats WHERE chat_id = ?",
                Long.class,
                chatId
            ));
        } catch (EmptyResultDataAccessException e) {
            saveId = Optional.empty();
        }

        return saveId.isEmpty();
    }
}
