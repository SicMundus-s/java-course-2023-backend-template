package edu.java.scrapper.dao.jdbc;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.mapper.rowmapper.ChatRowMapper;
import java.sql.PreparedStatement;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcChatDaoTest extends IntegrationEnvironment {

    @Autowired
    private JdbcChatDao chatDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void registerTest() {
        Long chatId = 12345L;
        chatDao.register(chatId);

        List<Chat> chats = jdbcTemplate.query(
            "SELECT id, chat_id FROM chats WHERE chat_id = ?",
            new ChatRowMapper(),
            chatId
        );
        assertFalse(chats.isEmpty());
        assertEquals(chatId, chats.getFirst().getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        Long chatId = 67890L;
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);

        Chat chat = chatDao.findChatByChatId(chatId);
        assertNotNull(chat);

        chatDao.removeChat(chat.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            jdbcTemplate.queryForObject(
                "SELECT id, chat_id FROM chats WHERE chat_id = ?",
                new ChatRowMapper(),
                chatId
            );
        });
    }

    @Test
    @Transactional
    @Rollback
    void findChatByChatIdTest() {
        Long chatId = 12345L;
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);

        Chat chat = chatDao.findChatByChatId(chatId);
        assertNotNull(chat);
        assertEquals(chatId, chat.getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void isNotRegisteredTest() {
        Long chatId = 12345L;
        assertTrue(chatDao.isNotRegistered(chatId));

        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId);

        assertFalse(chatDao.isNotRegistered(chatId));
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsByLinkIdTest() {
        Long chatId1 = 10001L;
        Long chatId2 = 10002L;

        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId1);
        jdbcTemplate.update("INSERT INTO chats (chat_id) VALUES (?)", chatId2);

        Long chatDbId1 = jdbcTemplate.queryForObject("SELECT id FROM chats WHERE chat_id = ?", Long.class, chatId1);
        Long chatDbId2 = jdbcTemplate.queryForObject("SELECT id FROM chats WHERE chat_id = ?", Long.class, chatId2);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO links (url, resource_type) VALUES (?, ?::resource_type)",
                    new String[] {"id"}
                );
                ps.setString(1, "https://example.com");
                ps.setString(2, "GITHUB");
                return ps;
            },
            keyHolder
        );
        Long linkId = keyHolder.getKey().longValue();

        jdbcTemplate.update(
            "INSERT INTO links (url, resource_type) VALUES  (?, ?::resource_type)",
            "TestData",
            "GITHUB"
        );

        jdbcTemplate.update("INSERT INTO chat_links (chat_id, link_id) VALUES (?, ?)", chatDbId1, linkId);
        jdbcTemplate.update("INSERT INTO chat_links (chat_id, link_id) VALUES (?, ?)", chatDbId2, linkId);

        List<Chat> chats = chatDao.findAllChatsByLinkId(linkId);
        assertFalse(chats.isEmpty());
        assertTrue(chats.stream().anyMatch(chat -> chat.getChatId().equals(chatId1)));
        assertTrue(chats.stream().anyMatch(chat -> chat.getChatId().equals(chatId2)));

        assertEquals(2, chats.size());
    }
}
